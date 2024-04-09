package cn.granitech.variantorm.persistence;

import cn.granitech.variantorm.pojo.Pagination;

import java.util.List;
import java.util.Map;

public interface RecordQuery {
    EntityRecord queryOne(String entityName, String filter, Map<String, Object> paramMap, String sort, String... fieldNames);

    List<EntityRecord> query(String entityName, String filter, Map<String, Object> paramMap, String sort, Pagination pagination, String... fieldNames);
}

