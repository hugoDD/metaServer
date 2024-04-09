package cn.granitech.trigger.business.trigger.action;

import cn.granitech.trigger.business.trigger.aviator.AviatorUtils;
import cn.granitech.util.JsonHelper;
import cn.granitech.util.RegexHelper;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.cache.OptionCacheManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DataOperateUtil {


    private static final String VALUE = "value";


    public static Map<String, Object> prepareUpdateFieldMap(Entity entity, Map<String, Object> entityMap, Map<String, List<DataOperateItem>> itemMap) {
        Map<String, Object> updateFieldMap = new HashMap<>();

        updateForCompileFields(itemMap.get("forCompile"), entityMap, updateFieldMap);

        updateForFieldFields(entity, itemMap.get("forField"), entityMap, updateFieldMap);

        updateForFixedFields(itemMap.get("toFixed"), entity, updateFieldMap);

        updateToNullFields(itemMap.get("toNull"), updateFieldMap);
        return updateFieldMap;
    }


    private static void updateForCompileFields(List<DataOperateItem> items, Map<String, Object> entityMap, Map<String, Object> updateFieldMap) {
        if (items != null) {
            for (DataOperateItem dataOperateItem : items) {
                Object eval = AviatorUtils.eval(dataOperateItem.getSourceField(), entityMap);
                updateFieldMap.put(dataOperateItem.getTargetField(), eval);
            }
        }
    }


    private static void updateForFieldFields(Entity entity, List<DataOperateItem> items, Map<String, Object> entityMap, Map<String, Object> updateFieldMap) {
        if (items != null) {
            try {
                items.forEach(item -> {
                    if (!entity.containsField(item.getTargetField())) {
                        return;
                    }

                    if (entity.getField(item.getTargetField()).getType() == FieldTypes.OPTION) {
                        OptionCacheManager optionCacheManager = SpringHelper.getBean(PersistenceManager.class).getOptionCacheManager();
                        List<OptionModel> options = optionCacheManager.getOptions(entity.getName(), item.getTargetField());
                        if (CollUtil.isEmpty(options)) {
                            return;
                        }
                        Object sourceValue = entityMap.get(item.getSourceField());
                        if (ObjectUtil.isNull(sourceValue)) {
                            return;
                        }
                        String labelValue = (sourceValue instanceof OptionModel) ? ((OptionModel) sourceValue).getLabel() : sourceValue.toString();
                        for (OptionModel option : options) {
                            if (option.getLabel().equals(labelValue)) {
                                updateFieldMap.put(item.getTargetField(), option.getValue());
                                break;
                            }
                        }
                    } else {
                        updateFieldMap.put(item.getTargetField(), entityMap.get(item.getSourceField()));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private static void updateForFixedFields(List<DataOperateItem> items, Entity entity, Map<String, Object> updateFieldMap) {
        if (items != null) {
            items.forEach(item -> {
                if (!entity.containsField(item.getTargetField().trim())) {
                    return;
                }
                Field field = entity.getField(item.getTargetField().trim());
                if (field.getType().equals(FieldTypes.REFERENCE)) {
                    IDName idName = JsonHelper.readJsonValue(item.getSourceField(), IDName.class);
                    updateFieldMap.put(item.getTargetField(), idName.getId().getId());
                } else if (field.getType().equals(FieldTypes.OPTION)) {
                    Map<String, Object> optionMap = JsonHelper.readJsonValue(item.getSourceField(), Map.class);
                    updateFieldMap.put(item.getTargetField(), optionMap.get("value"));
                } else if (field.getType().equals(FieldTypes.TAG)) {
                    List<Map<String, Object>> tagList =  JsonHelper.readJsonValue(item.getSourceField(), List.class);
                    assert tagList != null;
                    String tag = tagList.stream().map((map) -> (String)map.get("value")).collect(Collectors.joining(","));
                    updateFieldMap.put(item.getTargetField(), tag);
                } else {
                    updateFieldMap.put(item.getTargetField(), field.getType().fromJson(item.getSourceField()));
                }
            });
        }
    }


    private static void updateToNullFields(List<DataOperateItem> items, Map<String, Object> updateFieldMap) {
        if (items != null) {
            items.forEach(item -> updateFieldMap.put(item.getTargetField(), null));
        }
    }


    public static List<String> prepareQueryFields(DataOperate dataOperate, Map<String, List<DataOperateItem>> itemMap) {
        List<String> queryFieldList = new ArrayList<>();

        if (!dataOperate.isReferenced() && StringUtils.isNotBlank(dataOperate.getFieldName())) {
            queryFieldList.add(dataOperate.getFieldName());
        }
        setCompileFields(itemMap.get("forCompile"), queryFieldList);
        setForFields(itemMap.get("forField"), queryFieldList);
        if (CollectionUtils.isEmpty(queryFieldList)) {
            return Collections.EMPTY_LIST;
        }
        return queryFieldList;
    }


    private static void setCompileFields(List<DataOperateItem> forCompileList, List<String> queryFieldList) {
        if (forCompileList != null) {
            Set<String> variableSet = new HashSet<>();
            for (DataOperateItem dataOperateItem : forCompileList) {
                variableSet.addAll(RegexHelper.getVariableSet(dataOperateItem.getSourceField(), "\\{([a-zA-Z0-9$\\.]+)}"));
            }
            queryFieldList.addAll(variableSet);
        }
    }


    private static void setForFields(List<DataOperateItem> forFieldList, List<String> queryFieldList) {
        if (forFieldList != null) {
            List<String> fieldNameList = forFieldList.stream().map(DataOperateItem::getSourceField).collect(Collectors.toList());
            queryFieldList.addAll(fieldNameList);
        }
    }
}



