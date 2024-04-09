package cn.granitech.util;

import cn.granitech.business.service.*;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.granitech.variantorm.pojo.TagModel;
import cn.granitech.web.pojo.KeyValueEntry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CopyEntityHelper {
    public static final int APPROVAL_PROCESS = 4;
    public static final int FORM_DESIGN = 1;
    public static final int LIST_DESIGN = 2;
    private static final EntityManagerService entityManagerService = SpringHelper.getBean(EntityManagerService.class);
    private static final FormLayoutManager formLayoutManager = SpringHelper.getBean(FormLayoutManager.class);
    private static final LayoutService layoutService = SpringHelper.getBean(LayoutService.class);
    private static final OptionManagerService optionManagerService = SpringHelper.getBean(OptionManagerService.class);
    private static final TagManagerService tagManagerService = SpringHelper.getBean(TagManagerService.class);

    public static void copyPlainFields(Entity oldEntity, Entity newEntity) {
        Iterator<Field> var2 = oldEntity.getFieldSet().iterator();

        while (true) {
            Field field;
            do {
                do {
                    if (!var2.hasNext()) {
                        return;
                    }

                    field = var2.next();
                } while (MetadataHelper.isIgnoreField(field));
            } while (field.isMainDetailFieldFlag());

            if (!field.getType().equals(FieldTypes.OPTION)) {
                if (field.getType().equals(FieldTypes.TAG)) {
                    List<TagModel> oldTagList = tagManagerService.getTagList(oldEntity.getName(), field.getName());
                    List<String> values = oldTagList.stream().map(TagModel::getValue).collect(Collectors.toList());
                    tagManagerService.saveTagList(newEntity.getName(), field.getName(), values);
                }
            } else {
                List<OptionModel> oldTagList = optionManagerService.getOptionList(oldEntity.getName(), field.getName());
                List<KeyValueEntry<String, Integer>> optionList = new ArrayList<>();

                for (OptionModel optionModel : oldTagList) {
                    optionList.add(new KeyValueEntry(optionModel.getLabel(), optionModel.getValue()));
                }

                optionManagerService.saveOptionList(newEntity.getName(), field.getName(), optionList);
            }

            field.setPhysicalName("c_" + field.getName());
            field.setOwner(newEntity);
            entityManagerService.createPlainField(newEntity.getEntityCode(), field);
        }
    }

    public static void copyEntityData(Entity sourceEntity, Entity oldEntity, String mainEntityName) {
        sourceEntity.setPhysicalName(null);
        sourceEntity.setEntityCode(null);
        sourceEntity.setEntityId(null);
        sourceEntity.setLayoutable(oldEntity.isLayoutable());
        sourceEntity.setAuthorizable(oldEntity.isAuthorizable());
        sourceEntity.setListable(oldEntity.isListable());
        entityManagerService.createEntity(sourceEntity, mainEntityName);
    }

    public static void handleFormDesign(Entity oldEntity, Entity newEntity) {
        formLayoutManager.saveLayout(newEntity.getName(), formLayoutManager.getLayout(oldEntity.getName()).getLayoutJson());
    }

    public static void handleListDesign(Entity oldEntity, Entity newEntity) {
        EntityRecord layoutEntityRecord = layoutService.getLayoutEntityRecord(oldEntity.getName(), "LIST", "ALL");
        if (layoutEntityRecord != null) {
            layoutEntityRecord.setFieldValue(layoutEntityRecord.getEntity().getIdField().getName(), null);
            layoutEntityRecord.setFieldValue("entityCode", newEntity.getEntityCode());
            layoutService.createRecord(layoutEntityRecord);
        }
    }

    public static void handleApprovalProcess(Entity oldEntity, Entity newEntity) {
        if (MetadataHelper.hasApprovalField(oldEntity)) {
            entityManagerService.createApprovalSystemFields(newEntity.getEntityCode().intValue());
        }
        for (EntityRecord configRecord : entityManagerService.queryListRecord("ApprovalConfig", String.format(" entityCode = '%s'  AND  approvalFlowId is not null ", oldEntity.getEntityCode()), null, null, null)) {
            EntityRecord flowRecord = entityManagerService.queryOneRecord("ApprovalFlow", String.format(" approvalConfigId = '%s' ", configRecord.id()), null, " [createdOn] DESC ");
            configRecord.setFieldValue(configRecord.getEntity().getIdField().getName(), null);
            configRecord.setFieldValue("entityCode", newEntity.getEntityCode());
            ID newConfigId = entityManagerService.createRecord(configRecord);
            flowRecord.setFieldValue(flowRecord.getEntity().getIdField().getName(), null);
            flowRecord.setFieldValue("approvalConfigId", newConfigId);
            entityManagerService.createRecord(flowRecord);
        }
    }
}
