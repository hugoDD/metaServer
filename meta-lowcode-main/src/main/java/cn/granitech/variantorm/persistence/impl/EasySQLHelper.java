package cn.granitech.variantorm.persistence.impl;



import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.queryCompiler.SelectStatement;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import org.apache.commons.lang3.StringUtils;

public class EasySQLHelper {
    private static String generateSql(QuerySchema querySchema, Pagination pagination) {
        StringBuilder sql = new StringBuilder();
        String selectFields =  querySchema.getSelectFields().trim();
        if (",".equals(selectFields.substring(selectFields.length() - 1))) {
            selectFields = selectFields.substring(0, selectFields.length() - 1);
        }
        sql.append(String.format("SELECT %s FROM %s ", selectFields,querySchema.getMainEntity()));
        if (!StringUtils.isBlank(querySchema.getFilter())) {
            sql.append(String.format("WHERE %s ", querySchema.getFilter()));
        }
        if (!StringUtils.isBlank(querySchema.getGroupBy())) {
            sql.append(String.format("GROUP BY %s ", querySchema.getGroupBy()));
        }
        if (!StringUtils.isBlank(querySchema.getSort())) {
            sql.append(String.format("ORDER BY %s ", querySchema.getSort()));
        }
//        if (pagination != null) {
//            int offset = (pagination.getPageNo() - 1) * pagination.getPageSize();
//            sql.append(String.format("LIMIT %d, %d ", offset,pagination.getPageSize()));
//        }
        String whereSql = sql.toString().replaceAll("\\[", "").replaceAll("]", "");
        whereSql = whereSql.replaceAll(":", "pn_");
        return whereSql;
    }



    public static SelectStatement generateRawSql(PersistenceManager pm, QuerySchema querySchema, Pagination pagination) {
        String sql = generateSql(querySchema, pagination);
        return pm.getSqlCompiler().compileEasySql(pm.getMetadataManager(), sql);
    }

    public static String getCountSql(String rawSql) {
        StringBuilder a = new StringBuilder();
        a.append(" SELECT count(*) ");
        StringBuilder var10000;
        if (!rawSql.contains(" ORDER BY ")) {
            if (rawSql.contains(" LIMIT ")) {
                var10000 = a;
                a.append(rawSql, rawSql.indexOf(" FROM "), rawSql.indexOf(" LIMIT "));
            } else {
                var10000 = a;
                a.append(rawSql.substring(rawSql.indexOf(" FROM ")));
            }
        } else {
            var10000 = a;
            a.append(rawSql, rawSql.indexOf(" FROM "), rawSql.indexOf(" ORDER BY "));
        }

        return var10000.toString();
    }

    public EasySQLHelper() {
    }


}
