package cn.granitech.variantorm.persistence.cache;

import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.OptionModel;

import java.util.List;

public interface QueryCache {
    OptionCacheManager getOptionCacheManager();

    void reloadReferenceListCache(String var1, String var2, String var3);

    boolean updateIDNameList(String var1, String var2, String var3, List<IDName> var4);

    void deleteIDName(String var1);

    void initIDNameList();

    OptionModel getOption(String var1, String var2, int var3);

    boolean updateIDName(IDName var1);

    OptionModel getStatus(String var1, String var2, int var3);

    TagCacheManager getTagCacheManager();

    List<IDName> getIDNameList(String var1, String var2, String var3);

    IDName getIDName(String var1);

    void initIDName();
}
