package cn.granitech.variantorm.persistence.cache;

import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.OptionModel;

import java.util.List;

public interface QueryCache {
    OptionCacheManager getOptionCacheManager();

    void reloadReferenceListCache(String entityName, String fieldName, String objectId);

    boolean updateIDNameList(String entityName, String refFieldName, String recordId, List<IDName> idNameList);

    void deleteIDName(String id);

    void initIDNameList();

    OptionModel getOption(String entityName, String fieldName, int optionValue);

    boolean updateIDName(IDName idName);

    OptionModel getStatus(String entityName, String fieldName, int statusValue);

    TagCacheManager getTagCacheManager();

    List<IDName> getIDNameList(String entityName, String refFieldName, String recordId);

    IDName getIDName(String refId);

    void initIDName();
}
