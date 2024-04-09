package cn.granitech.business.service;

import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.MetadataHelper;
import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.exception.InvalidFieldException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.web.pojo.RevisionHistoryContent;
import cn.granitech.web.pojo.vo.RevisionHistoryContentVO;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RevisionHistoryService extends BaseService {
    public static final String HIS_ENTITY_CODE = "entityCode";
    public static final String HIS_ENTITY_ID = "entityId";
    public static final String HIS_REVISION_BY = "revisionBy";
    public static final String HIS_REVISION_CONTENT = "revisionContent";
    public static final String HIS_REVISION_ON = "revisionOn";
    public static final String HIS_REVISION_TYPE = "revisionType";
    public static final int REVISION_TYPE_ALLOCATION = 5;
    public static final int REVISION_TYPE_BACK = 3;
    public static final int REVISION_TYPE_REVOCATION = 4;
    public static final int REVISION_TYPE_SUCCESS = 2;
    public static final int REVISION_TYPE_UPDATE = 1;
    private final Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    CallerContext callerContext;
    @Resource
    CrudService crudService;
    @Resource
    PersistenceManager pm;

    public List<RevisionHistoryContentVO> detailsById(ID revisionHistoryId) {
        List<RevisionHistoryContentVO> contentVOList = new ArrayList<>();
        EntityRecord entityRecord = super.queryRecordById(revisionHistoryId);
        Integer entityCode = entityRecord.getFieldValue("entityCode");
        Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
        String revisionContent = entityRecord.getFieldValue("revisionContent");
        if (StrUtil.isEmpty(revisionContent)) {
            return contentVOList;
        } else {
            List<RevisionHistoryContent> historyContentList = JsonHelper.readJsonValue(revisionContent, new TypeReference<List<RevisionHistoryContent>>() {
            });

            assert historyContentList != null;
            for (RevisionHistoryContent historyContent : historyContentList) {
                String fldName = historyContent.getFileId();
                if (entity.containsField(fldName)) {
                    Field field = entity.getField(fldName);

                    try {
                        RevisionHistoryContentVO contentVO = new RevisionHistoryContentVO();
                        if (field.getType() != FieldTypes.REFERENCE && field.getType() != FieldTypes.ANYREFERENCE) {
                            if (field.getType() == FieldTypes.REFERENCELIST) {
                                List<String> beforeList = (List<String>) historyContent.getBefore();
                                String beforeStr = beforeList.stream().map(this::getName).collect(Collectors.joining(", "));
                                contentVO.setBefore(beforeStr);
                                List<String> afterList = (List<String>) historyContent.getAfter();
                                String afterStr = afterList.stream().map(this::getName).collect(Collectors.joining(", "));
                                contentVO.setAfter(afterStr);
                            } else {
                                contentVO.setBefore(historyContent.getBefore());
                                contentVO.setAfter(historyContent.getAfter());
                            }
                        } else {
                            IDName beforeName = this.crudService.getIDName((String) historyContent.getBefore());
                            contentVO.setBefore(beforeName.getName());
                            IDName afterName = this.crudService.getIDName((String) historyContent.getAfter());
                            contentVO.setAfter(afterName.getName());
                        }

                        contentVO.setLabel(entity.getField(fldName).getLabel());
                        contentVOList.add(contentVO);
                    } catch (InvalidFieldException var17) {
                        this.log.error("修改历史映射失败，字段={}", fldName);
                    }
                }
            }


        }
        return contentVOList;
    }


    public String getName(String id) {
        return this.crudService.getIDName(id).getName();
    }


    public void insertApprovalHistory(ID recordId, Boolean isBack) {
        int revisionType = 4;
        if (isBack != null) {
            revisionType = isBack ? 3 : 2;
        }
        saveRevisionHistoryRecord(recordId, revisionType, null);
    }

    public void recordHistory(ID recordId, Integer revisionType, Map<String, Object> oldData, Map<String, Object> newData) {
        int entityCode = recordId.getEntityCode();
        Entity entity = this.pm.getMetadataManager().getEntity(entityCode);
        if (!(SystemEntities.isSystemEntity(entity.getName()) && entityCode != 21 && entityCode != 22 && entityCode != 24)) {
            List<RevisionHistoryContent> contentList = new ArrayList<>();
            for (String key : oldData.keySet()) {
                if (!MetadataHelper.isIgnoreField(entity.getField(key)) && newData.containsKey(key)) {
                    Object oldValue = oldData.get(key);
                    Object newValue = newData.get(key);
                    if (!Objects.equals(oldValue, newValue)) {
                        RevisionHistoryContent content = new RevisionHistoryContent();
                        content.setFileId(key);
                        content.setBefore(oldValue);
                        content.setAfter(newValue);
                        contentList.add(content);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(contentList)) {
                saveRevisionHistoryRecord(recordId, revisionType, JsonHelper.writeObjectAsString(contentList));
            }
        }
    }

    private void saveRevisionHistoryRecord(ID recordId, Integer revisionType, String content) {
        EntityRecord revisionHistory = this.pm.newRecord("RevisionHistory");
        revisionHistory.setFieldValue("entityCode", recordId.getEntityCode());
        revisionHistory.setFieldValue("entityId", recordId);
        revisionHistory.setFieldValue(HIS_REVISION_TYPE, revisionType);
        revisionHistory.setFieldValue(HIS_REVISION_CONTENT, content);
        revisionHistory.setFieldValue(HIS_REVISION_BY, ID.valueOf(this.callerContext.getCallerId()));
        revisionHistory.setFieldValue(HIS_REVISION_ON, new Date());
        this.pm.insert(revisionHistory);
    }
}
