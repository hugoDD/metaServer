package cn.granitech.business.service;

import cn.granitech.util.RedisUtil;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.cache.OptionCacheManager;
import cn.granitech.variantorm.persistence.cache.QueryCache;
import cn.granitech.variantorm.persistence.cache.TagCacheManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.granitech.web.enumration.RedisKeyEnum;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryCacheImpl implements QueryCache {
    private final RedisUtil redisUtil;
    private final PersistenceManager pm;
    private final OptionCacheManager optionCacheManager;
    private final TagCacheManager tagCacheManager;

    public QueryCacheImpl(RedisUtil redisUtil, PersistenceManager pm) {
        this.redisUtil = redisUtil;
        this.optionCacheManager = new OptionCacheManager(pm);
        this.tagCacheManager = new TagCacheManager(pm);
        this.pm = pm;
    }

    public void initIDName() {
        this.pm.getMetadataManager().getEntitySet().forEach((entity) -> {
            Field idField = entity.getIdField();
            Field nameField = ObjectUtils.isEmpty(entity.getNameField()) ? idField : entity.getNameField();
            List<EntityRecord> recordList = this.pm.createRecordQuery().query(entity.getName(), null, null, null, null, idField.getName(), nameField.getName());
            recordList.forEach((entityRecord) -> {
                ID id = entityRecord.getFieldValue(idField.getName());
                Object fieldName = entityRecord.getFieldValue(nameField.getName());
                String name = fieldName == null ? id.getId() : fieldName.toString();
                this.updateIDName(new IDName(id, name));
            });
        });
    }

    public void initIDNameList() {
        List<EntityRecord> list = this.pm.createRecordQuery().query("ReferenceListMap", null, null, null, null);
        Map<String, List<EntityRecord>> group = list.stream().collect(Collectors.groupingBy((entityRecordx) -> this.getReferenceListKey(entityRecordx.getFieldValue("entityName"), entityRecordx.getFieldValue("fieldName"), (entityRecordx.getFieldValue("objectId")).toString())));

        for (String key : group.keySet()) {
            List<IDName> idNameList = group.get(key).stream().map((entityRecordx) -> {
                ID toId = entityRecordx.getFieldValue("toId");
                IDName idName = this.getIDName(toId.toString());
                return idName == null ? new IDName(toId, null) : idName;
            }).collect(Collectors.toList());
            EntityRecord entityRecord = group.get(key).get(0);
            this.updateIDNameList(entityRecord.getFieldValue("entityName"), entityRecord.getFieldValue("fieldName"), entityRecord.getFieldValue("objectId").toString(), idNameList);
        }

    }

    public IDName getIDName(String referenceId) {
        String name = this.redisUtil.get(RedisKeyEnum.REFERENCE_CACHE.getKey(referenceId));
        IDName idName;
        if (StringUtils.isBlank(name)) {
            idName = this.loadIDName(ID.valueOf(referenceId));
        } else {
            idName = new IDName(ID.valueOf(referenceId), name);
        }

        return idName;
    }

    public boolean updateIDName(IDName idName) {
        return this.redisUtil.set(RedisKeyEnum.REFERENCE_CACHE.getKey(idName.getId().toString()), idName.getName());
    }

    public void deleteIDName(String id) {
        this.redisUtil.remove(RedisKeyEnum.REFERENCE_CACHE.getKey(id));
    }

    public List<IDName> getIDNameList(String entityName, String refFieldName, String recordId) {
        return this.redisUtil.get(this.getReferenceListKey(entityName, refFieldName, recordId));
    }

    public boolean updateIDNameList(String entityName, String refFieldName, String recordId, List<IDName> idNameList) {
        return this.redisUtil.set(this.getReferenceListKey(entityName, refFieldName, recordId), idNameList);
    }

    private IDName loadIDName(final ID referenceId) {
        Entity entity = this.pm.getMetadataManager().getEntity(referenceId.getEntityCode());
        String filter = String.format("[%s] = :entityId", entity.getIdField().getName());
        Map<String, Object> paramMap = new HashMap<String, Object>() {
            {
                this.put("entityId", referenceId);
            }
        };
        Field idField = entity.getIdField();
        Field nameField = ObjectUtils.isEmpty(entity.getNameField()) ? idField : entity.getNameField();
        EntityRecord record = this.pm.createRecordQuery().queryOne(entity.getName(), filter, paramMap, null, idField.getName(), nameField.getName());
        if (record == null) {
            EntityRecord recycleBin = this.pm.createRecordQuery().queryOne("RecycleBin", "entityId = :entityId", paramMap, "deletedOn DESC", "entityName");
            String name = (recycleBin == null ? "" : recycleBin.getFieldValue("entityName")) + "[已删除]";
            return new IDName(referenceId, name);
        } else {
            Object name = record.getFieldValue(nameField.getName());
            IDName idName = new IDName(referenceId, name == null ? referenceId.getId() : name.toString());
            this.updateIDName(idName);
            return idName;
        }
    }

    public void reloadReferenceListCache(String entityName, String fieldName, String objectId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("entityName", entityName);
        paramMap.put("fieldName", fieldName);
        paramMap.put("objectId", objectId);
        List<EntityRecord> list = this.pm.createRecordQuery().query("ReferenceListMap", "entityName = :entityName AND fieldName = :fieldName AND objectId = :objectId", paramMap, null, null);
        List<IDName> idNameList = list.stream().map((entityRecord) -> {
            ID toId = entityRecord.getFieldValue("toId");
            IDName idName = this.getIDName(toId.toString());
            return idName == null ? new IDName(toId, null) : idName;
        }).collect(Collectors.toList());
        this.updateIDNameList(entityName, fieldName, objectId, idNameList);
    }

    public OptionCacheManager getOptionCacheManager() {
        return this.optionCacheManager;
    }

    public TagCacheManager getTagCacheManager() {
        return this.tagCacheManager;
    }


    public OptionModel getOption(String entityName, String fieldName, int optionValue) {
        for (OptionModel om : this.optionCacheManager.getOptions(entityName, fieldName)) {
            if (om.getValue() == optionValue) {
                return om;
            }
        }
        return null;
    }

    public OptionModel getStatus(String entityName, String fieldName, int statusValue) {
        for (OptionModel om : this.optionCacheManager.getStatuses(fieldName)) {
            if (om.getValue() == statusValue) {
                return om;
            }
        }
        return null;
    }

    private String getReferenceListKey(String entityName, String refFieldName, String recordId) {
        return RedisKeyEnum.REFERENCE_LIST_MAP.getKey(String.format("%s_%s_%s", entityName, refFieldName, recordId));
    }
}
