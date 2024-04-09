package cn.granitech.report.service;

import cn.granitech.business.service.BaseService;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.service.EntityManagerService;
import cn.granitech.report.utils.Report;
import cn.granitech.util.EntityHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReportService
        extends BaseService {
    private static final List<String> unTypes = Arrays.stream(new String[]{"PrimaryKey", "File", "Picture", "ReferenceList", "AnyReference"


    }).collect(Collectors.toList());
    @Resource
    PersistenceManager pm;
    @Resource
    EntityManagerService entityManagerService;
    @Resource
    CrudService crudService;

    public List<Map<String, Object>> getFieldListByEntity(Entity entity) {
        List<Map<String, Object>> resultList = new ArrayList<>();


        resultList.add(getFieldMap(entity, false));


        Set<Entity> detailEntitySet = entity.getDetailEntitySet();
        for (Entity detail : detailEntitySet) {
            resultList.add(getFieldMap(detail, true));
        }

        return resultList;
    }


    public List<Map<String, Object>> getFieldListByReference(Entity mainEntity) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Set<Entity> referToEntitySet = this.entityManagerService.getReferToEntitySet(mainEntity.getName(), Boolean.valueOf(false), Boolean.valueOf(false));
        if (referToEntitySet == null) {
            return resultList;
        }
        referToEntitySet.forEach(entity -> {
            if (entity.getMainEntity() != null && entity.getMainEntity().getEntityCode() == mainEntity.getEntityCode()) {
                return;
            }
            resultList.add(getFieldMap(entity, true));
        });
        return resultList;
    }


    private Map<String, Object> getFieldMap(Entity entity, boolean isReference) {
        List<Map<String, String>> entityList = new ArrayList<>();
        entity.getFieldSet().stream().forEach(field -> {
            String fieldType = field.getType().getName();

            if (unTypes.contains(fieldType)) {
                return;
            }

            if (fieldType.equals("Reference")) {
                Set<Entity> referTo = field.getReferTo();

                String referEntity = referTo.iterator().next().getName();

                if (referTo.size() > 0 && !referEntity.equals("User") && !referEntity.equals("TagItem") && !referEntity.equals("Department") && !referEntity.equals("ApprovalConfig")) {
                    return;
                }
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("name", field.getLabel());
            map.put("type", fieldType);
            map.put("code", (isReference ? (entity.getName() + ".") : "") + field.getName());
            entityList.add(map);
        });
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("label", entity.getLabel());
        resultMap.put("fieldList", entityList);
        return resultMap;
    }


    public Map<String, Object> getReportData(Report report, String reportConfigId, String entityId) {
        Map<String, Object> dataMap = new HashMap<>();

        Entity entity = this.pm.getMetadataManager().getEntity((new ID(entityId)).getEntityCode());
        List<String> fieldNameList = report.getFieldNameList();
        if (fieldNameList != null && fieldNameList.size() > 0) {
            EntityRecord entityRecord = this.crudService.queryById(new ID(entityId), fieldNameList.stream().toArray(x$0 -> new String[x$0]));
            Map<String, Object> fieldMap = entityRecord.getValuesMap();
            fieldMap.putAll(entityRecord.getLabelsMap());
            dataMap.put(entity.getName(), fieldMap);
        }

        Map<String, List<String>> subEntityFieldMap = report.getSubEntityFieldMap();
        if (subEntityFieldMap != null && subEntityFieldMap.values().size() > 0) {
            for (String entityName : subEntityFieldMap.keySet()) {
                List<Map<String, Object>> resultList = new ArrayList<>();
                String filter = " ";
                Entity subEntity = EntityHelper.getEntity(entityName);
                Collection<Field> fieldSet = subEntity.getFieldSet();
                for (Field field : fieldSet) {
                    if (isReferenceToEntity(field, entity)) {
                        filter = buildFilterForEntityId(field.getReferTo().iterator().next(), entityId);
                        break;
                    }
                }
                List<EntityRecord> entityRecords = this.crudService.queryListRecord(entityName, filter, null, null, null, (String[]) ((List) subEntityFieldMap
                        .get(entityName)).stream().toArray(x$0 -> new String[x$0]));
                entityRecords.forEach(record -> {
                    Map<String, Object> fieldMap = record.getValuesMap();
                    fieldMap.putAll(record.getLabelsMap());
                    resultList.add(fieldMap);
                });
                dataMap.put(entityName, resultList);
            }
        }
        return dataMap;
    }


    private boolean isReferenceToEntity(Field field, Entity entity) {
        if (field.getType() == FieldTypes.REFERENCE) {
            Entity referEntity = field.getReferTo().iterator().next();
            return referEntity.equals(entity);
        }
        return false;
    }


    private String buildFilterForEntityId(Entity referEntity, String entityId) {
        Field idField = referEntity.getIdField();
        return String.format(" %s = '%s' ", idField.getName(), entityId);
    }
}



