package cn.granitech.variantorm.persistence;

import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;

import java.util.List;
import java.util.Map;

public interface DataQuery {
    List<Map<String, Object>> query(QuerySchema paramQuerySchema, Pagination paramPagination);

    List<Map<String, Object>> query(QuerySchema paramQuerySchema, Pagination paramPagination, Map<String, Object> paramMap);
}
