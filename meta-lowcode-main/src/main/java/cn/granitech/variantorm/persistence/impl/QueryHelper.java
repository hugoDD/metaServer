package cn.granitech.variantorm.persistence.impl;

import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.*;
import cn.granitech.variantorm.util.MDHelper;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryHelper {
    public static final String FIELD_SP = ",";
    public static final String CASCADE_SYMBOL = ".";
    private static final String SELECT_FOUND_ROWS = "; SELECT FOUND_ROWS(); ";
    private static final String SQL_CALC_FOUND_ROWS = " SQL_CALC_FOUND_ROWS ";
    private static final String RIGHT_BRACKET = "]";
    private static final String LEFT_BRACKET = "\\[";
    private static final String BRACKET_REGEX = "\\[\\S+]";


    public static String generateSQL(PersistenceManager pm, QuerySchema querySchema, Pagination pagination, List<String> queryFields) {
        Map<String, String> fieldEntityAliasMap = new HashMap<>();
        Map<Integer, Set<JointModel>> jointModelMap = QueryHelper.jointModelMap(pm.getMetadataManager(), querySchema, fieldEntityAliasMap);
        StringBuilder sql = new StringBuilder();
        MetadataManager metadataManager = pm.getMetadataManager();
        sql.append(QueryHelper.generateSelect(metadataManager, fieldEntityAliasMap, querySchema, queryFields, pagination, jointModelMap));
        sql.append(QueryHelper.generateFrom(metadataManager, jointModelMap, querySchema));
        sql.append(QueryHelper.generateWhere(metadataManager, fieldEntityAliasMap, querySchema));
        sql.append(QueryHelper.generateGroup(metadataManager, fieldEntityAliasMap, querySchema));
        sql.append(QueryHelper.generateOrder(metadataManager, fieldEntityAliasMap, querySchema));
        if (pagination != null) {
            sql.append(QueryHelper.generatePagination(metadataManager, querySchema, pagination));
            sql.append(SELECT_FOUND_ROWS);
        }
        return sql.toString();
    }

    public static String getCountSql(String originalSql) {
        StringBuffer a = new StringBuffer();
        a.append(" SELECT count(*) ");
        if (!originalSql.contains(" ORDER BY ")) {
            if (originalSql.contains(" LIMIT ")) {
                a.append(originalSql, originalSql.indexOf(" FROM "), originalSql.indexOf(" LIMIT "));
            } else {
                a.append(originalSql.substring(originalSql.indexOf(" FROM ")));
            }
        } else {
            a.append(originalSql, originalSql.indexOf(" FROM "), originalSql.indexOf(" ORDER BY "));
        }

        return a.toString();
    }





    private static  String generateOrder(MetadataManager mdm, Map<String, String> fieldEntityAliasMap, QuerySchema querySchema) {
        if (StringUtils.isBlank(querySchema.getSort())) {
            return "";
        }

        SqlHelper.checkSqlInjection(querySchema.getSort());
        String orderSql = " ORDER BY " + querySchema.getSort();
        return QueryHelper.replaceCascadeField(mdm, fieldEntityAliasMap, querySchema.getMainEntity(), orderSql);
    }

    private static  List<String> getFilterField(QuerySchema querySchema) {
        List<String> a = new ArrayList<>();
        if (StringUtils.isBlank(querySchema.getFilter())) {
            return a;
        }
        QueryHelper.matcherField(querySchema.getFilter(), a, BRACKET_REGEX);
        return a;
    }


    private static  String generateFrom(MetadataManager mdm, Map<Integer, Set<JointModel>> jointModelMap, QuerySchema querySchema) {
        StringBuilder fromSql = new StringBuilder();
        String mainEntity = querySchema.getMainEntity();
        String physicalName = mdm.getEntity(mainEntity).getPhysicalName();
        fromSql.append(String.format(" FROM `%s` as `%s` ", physicalName,mainEntity));
        if (jointModelMap.isEmpty()) {
            return fromSql.toString();
        }



        for (Integer joinModelKey : jointModelMap.keySet()) {
            for (JointModel jointModel : jointModelMap.get(joinModelKey)) {
                String epName  = MDHelper.getEPName(mdm, jointModel.getJointEntity());
                String jointEntityAlias = jointModel.getJointEntityAlias();
                String selfEntityAlias = jointModel.getSelfEntityAlias();
                String selfFpName = MDHelper.getFPName(mdm, jointModel.getSelfEntity(), jointModel.getSelfField());
                String joinFpName = MDHelper.getFPName(mdm, jointModel.getJointEntity(), jointModel.getJointIdField());
                fromSql.append(String.format(" LEFT JOIN `%s` as `%s` on `%s`.`%s`=`%s`.`%s` ",
                        epName,jointEntityAlias,selfEntityAlias,selfFpName,jointEntityAlias,joinFpName));
            }
        }
        return fromSql.toString();
    }


    private static  List<String> getCascadeFieldList(MetadataManager mdm, QuerySchema querySchema) {
        Object a;
        int n;
        ArrayList<String> a2 = new ArrayList<>();
        String[] stringArray = querySchema.getSelectFields().replaceAll(" ", "").split(FIELD_SP);
        int n2 = stringArray.length;
        int n3 = n = 0;
        while (n3 < n2) {
            a = stringArray[n];
            if (!StringUtils.isEmpty((CharSequence)a)) {
                a2.add(((String)a).replaceAll(" ", "").replaceAll(LEFT_BRACKET, "").replaceAll(RIGHT_BRACKET, ""));
            }
            n3 = ++n;
        }
        ArrayList<String> a3 = new ArrayList<>();
        for (String a4 : a2) {
            a = MDHelper.getFieldTypeOfCascadeField(mdm, querySchema.getMainEntity(), a4);
            Entity a5 = mdm.getEntity(MDHelper.getEntityOfCascadeField(mdm, querySchema.getMainEntity(), a4));
            if (a == FieldTypes.REFERENCE) {
                a3.add(a4);
                String lastFieldOfCascadeField = MDHelper.getLastFieldOfCascadeField(a4);
                Entity a7 = a5.getField(lastFieldOfCascadeField).getReferTo().iterator().next();
                String a8 = a7.getIdField().getName();
                if (a7.getNameField() != null) {
                    a8 = a7.getNameField().getName();
                }
                String a9 = new StringBuilder().insert(0, a4).append(CASCADE_SYMBOL).append(a8).toString();
                a3.add(a9);
                continue;
            }
            a3.add(a4);
        }
        return a3;
    }

    private static  String referenceCascadeField(MetadataManager mdm, Map<String, String> fieldEntityAliasMap, String mainEntity, String fieldExp) {
        String entityOfCascadeField = MDHelper.getEntityOfCascadeField(mdm, mainEntity, fieldExp);
        String entityOfCascadeFieldAs = fieldEntityAliasMap.getOrDefault(fieldExp, entityOfCascadeField);
        String lastFieldOfCascadeField = MDHelper.getLastFieldOfCascadeField(fieldExp);
        String physicalName = mdm.getEntity(entityOfCascadeField).getField(lastFieldOfCascadeField).getPhysicalName();

        return String.format("`%s`.`%s` as `%s_%s`, ",
                entityOfCascadeFieldAs,physicalName,entityOfCascadeField,lastFieldOfCascadeField);
    }

    private static  String generateGroup(MetadataManager mdm, Map<String, String> fieldEntityAliasMap, QuerySchema querySchema) {
        if (StringUtils.isBlank(querySchema.getGroupBy())) {
            return "";
        }

        SqlHelper.checkSqlInjection(querySchema.getGroupBy());
        String groupSql = " GROUP BY " + querySchema.getGroupBy();
        return QueryHelper.replaceCascadeField(mdm, fieldEntityAliasMap, querySchema.getMainEntity(), groupSql);
    }

    private static  List<String> getSortField(QuerySchema querySchema) {
        List<String> sortFields = new ArrayList<>();
        if (StringUtils.isBlank(querySchema.getSort())) {
            return sortFields;
        }
        QueryHelper.matcherField(querySchema.getSort(), sortFields, BRACKET_REGEX);
        return sortFields;
    }

    private static  String generateCascadeField(MetadataManager mdm, Map<String, String> fieldEntityAliasMap, String mainEntity, String fieldExp) {
        String cascadeField = MDHelper.getEntityOfCascadeField(mdm, mainEntity, fieldExp);
        String fieldExpAlias = fieldEntityAliasMap.getOrDefault(fieldExp, cascadeField);
        String lastFieldOfCascadeField = MDHelper.getLastFieldOfCascadeField(fieldExp);
        String fpName = MDHelper.getFPName(mdm, cascadeField, lastFieldOfCascadeField);

        return String.format("`%s`.`%s`", fieldExpAlias,fpName);
    }

    private static  String replaceCascadeField(MetadataManager mdm, Map<String, String> fieldEntityAliasMap, String mainEntity, String inStr) {
        Matcher matcher = Pattern.compile(BRACKET_REGEX).matcher(inStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String group = matcher.group();
            group = group.replaceAll(" ", "").replaceAll(LEFT_BRACKET, "").replaceAll(RIGHT_BRACKET, "");
            matcher.appendReplacement(sb, QueryHelper.generateCascadeField(mdm, fieldEntityAliasMap, mainEntity, group));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static  String generateWhere(MetadataManager mdm, Map<String, String> fieldEntityAliasMap, QuerySchema querySchema) {
        if (StringUtils.isBlank(querySchema.getFilter())) {
            return "";
        }

        SqlHelper.checkSqlInjection(querySchema.getFilter());
        String mainEntity = querySchema.getMainEntity();
        StringBuilder sql = new StringBuilder();
        sql.append(" WHERE ").append(querySchema.getFilter());
        if (SystemEntities.hasDeletedFlag(mainEntity)) {
            sql.append(String.format(" and ((`%s`.`isDeleted` IS NULL) or (`%s`.`isDeleted` = 0)) ", mainEntity,mainEntity));
        }
        return QueryHelper.replaceCascadeField(mdm, fieldEntityAliasMap, querySchema.getMainEntity(), sql.toString());
    }

    private static  void handleFieldEntityAliasMap(MetadataManager mdm, String rootEntity, String cascadeField, Map<Integer, Set<JointModel>> jointMap, Map<String, String> fieldEntityAliasMap) {
        if (StringUtils.isEmpty(cascadeField)) {
            return;
        }
        int joinKey = 1;
        String tempCascdeField = cascadeField;
        String selfEntityAlias =rootEntity;
        String selfEntityName = rootEntity;
        while (tempCascdeField.contains(CASCADE_SYMBOL)) {
            Set<JointModel> set;
            String fieldName = cascadeField.substring(0, cascadeField.indexOf(CASCADE_SYMBOL));
            Field field = mdm.getEntity(selfEntityName).getField(fieldName);
            if (field.getType() != FieldTypes.REFERENCE){
                continue;
            }
            String entityName = field.getReferTo().iterator().next().getName();
            String jointIdFieldName = mdm.getEntity(entityName).getIdField().getName();
            if (jointMap.containsKey(joinKey)) {
                set = jointMap.get(joinKey);
            } else {
                set = new LinkedHashSet<>();
                jointMap.put(joinKey, set);
            }
            JointModel jointModel = QueryHelper.getJoinModel(set, selfEntityName, selfEntityAlias, fieldName, entityName, jointIdFieldName);
            tempCascdeField = tempCascdeField.substring(tempCascdeField.indexOf(CASCADE_SYMBOL) + 1);
            ++joinKey;
            selfEntityName = jointModel.getJointEntity();
            selfEntityAlias = jointModel.getJointEntityAlias();
        }
        fieldEntityAliasMap.put(cascadeField, selfEntityAlias);
    }

    private static  String generatePagination(MetadataManager mdm, QuerySchema querySchema, Pagination pagination) {
        if (pagination == null) {
            return "";
        }
        int offset = (pagination.getPageNo() - 1) * pagination.getPageSize();
        return String.format(" LIMIT %d, %d ", offset,pagination.getPageSize());
    }


    private static  Map<Integer, Set<JointModel>> jointModelMap(MetadataManager mdm, QuerySchema querySchema, Map<String, String> fieldEntityAliasMap) {
        TreeMap<Integer, Set<JointModel>> jointMap = new TreeMap<>();
        for (String cascadeField : QueryHelper.getCascadeFieldList(mdm, querySchema)) {
            QueryHelper.handleFieldEntityAliasMap(mdm, querySchema.getMainEntity(), cascadeField, jointMap, fieldEntityAliasMap);
        }
        List<String> cascadeFieldList = QueryHelper.getFilterField(querySchema);
        for (String cascadeField : cascadeFieldList) {
            QueryHelper.handleFieldEntityAliasMap(mdm, querySchema.getMainEntity(), cascadeField, jointMap, fieldEntityAliasMap);
        }
        List<String> sortFields = QueryHelper.getSortField(querySchema);
        for (String sortField : sortFields) {
            QueryHelper.handleFieldEntityAliasMap(mdm, querySchema.getMainEntity(), sortField, jointMap, fieldEntityAliasMap);
        }
        return jointMap;
    }


    private static  String generateSelect(MetadataManager mdm, Map<String, String> fieldEntityAliasMap, QuerySchema querySchema, List<String> queryFields, Pagination pagination, Map<Integer, Set<JointModel>> jointModelMap) {

        SqlHelper.checkSqlInjection( querySchema.getSelectFields());
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        if (pagination != null) {
            stringBuilder.append(SQL_CALC_FOUND_ROWS);
        }
        for (String cascadeField : QueryHelper.getCascadeFieldList(mdm, querySchema)) {
            FieldType fieldType = MDHelper.getFieldTypeOfCascadeField(mdm, querySchema.getMainEntity(), cascadeField);
            Entity entity = mdm.getEntity(MDHelper.getEntityOfCascadeField(mdm, querySchema.getMainEntity(), cascadeField));
            if (fieldType == FieldTypes.REFERENCELIST) {
                String idFieldName = entity.getIdField().getName();
                String fieldExp = cascadeField.substring(0, cascadeField.lastIndexOf(CASCADE_SYMBOL) + 1)+idFieldName;
//                String fieldExp = new StringBuilder().insert(0, cascadeField.substring(0, cascadeField.lastIndexOf(CASCADE_SYMBOL) + 1)).append(idFieldName).toString();
                stringBuilder.append(QueryHelper.referenceCascadeField(mdm, fieldEntityAliasMap, querySchema.getMainEntity(), fieldExp));
            } else if (fieldType == FieldTypes.REFERENCE) {
                stringBuilder.append(QueryHelper.referenceCascadeField(mdm, fieldEntityAliasMap, querySchema.getMainEntity(), cascadeField));
            } else {
                stringBuilder.append(QueryHelper.referenceCascadeField(mdm, fieldEntityAliasMap, querySchema.getMainEntity(), cascadeField));
            }
            queryFields.add(cascadeField);
        }
        return stringBuilder.substring(0, stringBuilder.length() - 2);
    }

    private static  JointModel getJoinModel(Set<JointModel> jointModelSet, String selfEntityName, String selfEntityAlias, String selfFieldName, String jointEntityName, String jointIdFieldName) {
        JointModel a;
        for (JointModel jointModel : jointModelSet) {
            if (!jointEntityName.equalsIgnoreCase(jointModel.getJointEntity()) || !selfEntityName.equalsIgnoreCase(jointModel.getSelfEntity()) || !selfFieldName.equalsIgnoreCase(jointModel.getSelfField())) continue;
            return jointModel;
        }
        int a3 = 0;
        for (JointModel a4 : jointModelSet) {
            if (!jointEntityName.equalsIgnoreCase(a4.getJointEntity()) || !selfEntityName.equalsIgnoreCase(a4.getSelfEntity())) continue;
            ++a3;
        }
        String string = new StringBuilder().insert(0, jointEntityName).append(String.valueOf(a3 + 1)).toString();
        JointModel jointModel = a = new JointModel(selfEntityName, selfEntityAlias, selfFieldName, jointEntityName, string, jointIdFieldName);
        jointModelSet.add(jointModel);
        return jointModel;
    }

    private static  void matcherField(String inStr, List<String> outList, String regEx) {
        Matcher matcher = Pattern.compile(regEx).matcher(inStr);
        while (matcher.find()) {
            String group = matcher.group();
            if (StringUtils.isEmpty(group)) {
                continue;
            }
            outList.add(group.replaceAll(" ", "").replaceAll(LEFT_BRACKET, "").replaceAll(RIGHT_BRACKET, ""));
        }
    }
}
