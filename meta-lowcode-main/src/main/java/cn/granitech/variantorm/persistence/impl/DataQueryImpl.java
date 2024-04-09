package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.DataQuery;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.cache.QueryCache;
import cn.granitech.variantorm.persistence.queryCompiler.SelectStatement;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.variantorm.util.MDHelper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.util.*;

public class DataQueryImpl implements DataQuery {
    private final PersistenceManager pm;
    private static final String SQL_CALC_FOUND_ROWS = " SQL_CALC_FOUND_ROWS ";
    private final QueryCache queryCache;
    private static final String SELECT_FOUND_ROWS = "; SELECT FOUND_ROWS(); ";


    @Override
    public List<Map<String, Object>> query(QuerySchema querySchema, Pagination pagination, Map<String, Object> paramMap) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        this.handleParam(paramMap);
        this.doQuery(querySchema, paramMap, pagination, resultList);
        return resultList;
    }

    @Override
    public List<Map<String, Object>> query(QuerySchema querySchema, Pagination pagination) {
        ArrayList<Map<String, Object>> resultList = new ArrayList<>();
        this.doQuery(querySchema, new HashMap<>(), pagination, resultList);
        return resultList;
    }

    static void doCascadeQuery(DataQueryImpl dataQuery, ResultSet resultSet, List<String> queryFields, PersistenceManager pm, QuerySchema querySchema, QueryCache queryCache, List<Map<String, Object>> resultList) {
        dataQuery.doCascadeQuery(resultSet, queryFields, pm, querySchema, queryCache, resultList);
    }

    private void handleParam(Map<String, Object> paramMap) {
        if (paramMap == null || paramMap.size() <= 0) {
            return;
        }
        for (String key : paramMap.keySet()) {
            Object value = paramMap.get(key);
            if (value instanceof ID) {
                paramMap.put(key, ((ID) value).getId());
                continue;
            }
            if (value instanceof Boolean) {
                paramMap.put(key, (Boolean) value ? 1 : 0);
                continue;
            }
            if (value != null) continue;
            paramMap.put(key, null);
        }
    }

    static QueryCache getQueryCache(DataQueryImpl x0) {
        return x0.queryCache;
    }

    /*
     * WARNING - void declaration
     */
    public DataQueryImpl(PersistenceManager persistenceManager, QueryCache queryCache) {
        this.pm = persistenceManager;
        this.queryCache = queryCache;
    }

    /*
     * WARNING - void declaration
     */
    private void doCascadeQuery(ResultSet resultSet, List<String> queryFields, PersistenceManager pm, QuerySchema querySchema, QueryCache queryCache, List<Map<String, Object>> resultList) {
        Map<String, Object> cascadeFieldMap = new LinkedHashMap<>();
        int paramInt = 1;
        int len = queryFields.size();
        for (;paramInt<=len;) {
            String cascadeField = queryFields.get(paramInt-1);
            FieldType fieldType = MDHelper.getFieldTypeOfCascadeField(pm.getMetadataManager(), querySchema.getMainEntity(), cascadeField);
            String entityName = MDHelper.getEntityOfCascadeField(pm.getMetadataManager(), querySchema.getMainEntity(), cascadeField);
            String fieldName = MDHelper.getLastFieldOfCascadeField(cascadeField);
            Field field = pm.getMetadataManager().getEntity(entityName).getField(fieldName);
            Object value = fieldType.readDBValue(pm, field, resultSet, paramInt);
            cascadeFieldMap.put(cascadeField, value);
            if (field.getType() == FieldTypes.REFERENCE) {
                paramInt += 2;
            } else {
                ++paramInt;
            }
        }
        resultList.add(cascadeFieldMap);
    }

    static PersistenceManager getPersistenceManager(DataQueryImpl x0) {
        return x0.pm;
    }

    private void doQuery(QuerySchema querySchema, Map<String, Object> paramMap, Pagination pagination, List<Map<String, Object>> resultList) {
        SelectStatement selectStatement = EasySQLHelper.generateRawSql(this.pm, querySchema, pagination);
        List<String> queryFields = selectStatement.getFieldsList();
        String sql = selectStatement.toString();
        if (pagination != null) {
            sql = sql.replaceFirst("SELECT ", "SELECT  " + SQL_CALC_FOUND_ROWS);
            sql = sql + SELECT_FOUND_ROWS;
        }
        System.out.println("Raw sql: " + sql);
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(this.pm.getDataSource());
        jdbcTemplate.execute(sql, paramMap, preparedStatement -> {
            boolean execute = preparedStatement.execute();
            if (execute) {
                ResultSet rs = preparedStatement.getResultSet();
                while (rs != null && rs.next()) {
                    doCascadeQuery(rs, queryFields, pm, querySchema, queryCache, resultList);
                }
                if (preparedStatement.getMoreResults()) {
                    rs = preparedStatement.getResultSet();
                    if (rs != null && rs.next()) {
                        if (pagination != null) {
                            pagination.setTotal(rs.getInt(1));
                        }
                    }
                }


            }

            return null;
        });
    }
}
