package cn.granitech.variantorm.persistence;


import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.persistence.cache.OptionCacheManager;
import cn.granitech.variantorm.persistence.cache.QueryCache;
import cn.granitech.variantorm.persistence.cache.TagCacheManager;
import cn.granitech.variantorm.persistence.dialect.Dialect;
import cn.granitech.variantorm.persistence.license.LicenseInfo;
import cn.granitech.variantorm.persistence.queryCompiler.SqlCompiler;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public interface PersistenceManager {
    MetadataManager getMetadataManager();

    SqlCompiler getSqlCompiler();

    Dialect getDialect();

    DataSource getDataSource();

    JdbcTemplate getJdbcTemplate();

    EntityRecord newRecord(String var1);

    ID insert(EntityRecord var1);

    Boolean update(EntityRecord var1);

    Boolean delete(ID var1, String... var2);

    void restore(ID var1);

    DataQuery createDataQuery();

    RecordQuery createRecordQuery();

    boolean batchDelete(String var1, String var2);

    boolean batchAssign(String var1, String var2, ID var3, ID var4);

    OptionCacheManager getOptionCacheManager();

    TagCacheManager getTagCacheManager();

    QueryCache getQueryCache();

    LicenseInfo validateLicense(String var1, String var2, String var3, String var4, Integer var5, String var6);

    LicenseInfo getLicenseInfo();
}
