package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.impl.MetadataManagerImpl;
import cn.granitech.variantorm.persistence.DataQuery;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.persistence.RecordQuery;
import cn.granitech.variantorm.persistence.cache.OptionCacheManager;
import cn.granitech.variantorm.persistence.cache.QueryCache;
import cn.granitech.variantorm.persistence.cache.QueryCacheImpl;
import cn.granitech.variantorm.persistence.cache.TagCacheManager;
import cn.granitech.variantorm.persistence.dialect.Dialect;
import cn.granitech.variantorm.persistence.dialect.MySQLDialect;
import cn.granitech.variantorm.persistence.license.LicenseHelper;
import cn.granitech.variantorm.persistence.license.LicenseInfo;
import cn.granitech.variantorm.persistence.queryCompiler.SqlCompiler;
import cn.granitech.variantorm.persistence.queryCompiler.SqlCompilerImpl;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.text.MessageFormat;

public class PersistenceManagerImpl implements PersistenceManager {
    private final SqlCompiler sqlCompiler;
    private final DataSource dataSource;
    private QueryCache queryCache;
    private LicenseInfo licenseInfo = LicenseInfo.buildFreeLicense("");
    private final Dialect dialect;
    private final PersistenceManager persistenceManager;
    private final MetadataManager metadataManager;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SqlCompiler getSqlCompiler() {
        return this.sqlCompiler;
    }

    static  PersistenceManager getPersistenceManager(PersistenceManagerImpl x0) {
        return x0.persistenceManager;
    }


    @Override
    public Boolean update(EntityRecord entityRecord) {

        this.jdbcTemplate.execute(new UpdateCallBack(this, entityRecord));

        return true;
    }

    @Override
    public Dialect getDialect() {
        return this.dialect;
    }

    @Override
    public RecordQuery createRecordQuery() {
        PersistenceManagerImpl persistenceManagerImpl = this;
        return new RecordQueryImpl(persistenceManagerImpl, persistenceManagerImpl.queryCache);
    }

    @Override
    public TagCacheManager getTagCacheManager() {
        return this.queryCache.getTagCacheManager();
    }

    @Override
    public EntityRecord newRecord(String entityName) {
        return new EntityRecordImpl(this, entityName);
    }

    @Override
    public Boolean delete(ID recordId, String ... entityName) {
        Entity entity = this.metadataManager.getEntity(recordId.getEntityCode());
        if (entityName != null && entityName.length > 0 && !entity.getName().equals(entityName[0])) {
            throw new IllegalArgumentException("Mismatch between recordId and entityName!");
        }
        return this.jdbcTemplate.execute(new M(this, entity, recordId));
    }

    @Override
    public LicenseInfo getLicenseInfo() {
        return this.licenseInfo;
    }

    public void setQueryCache(QueryCache queryCache) {
        this.queryCache = queryCache;
    }


    public PersistenceManagerImpl(DataSource dataSource, SqlCompiler sqlCompiler) {

        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource");
        }
        this.metadataManager = new MetadataManagerImpl();
        this.dialect = new MySQLDialect();
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.persistenceManager = this;
        this.sqlCompiler = sqlCompiler;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public MetadataManager getMetadataManager() {
        return this.metadataManager;
    }

    @Override
    public void restore(ID recordId) {
        PersistenceManagerImpl persistenceManagerImpl = this;
        Entity a = persistenceManagerImpl.metadataManager.getEntity(recordId.getEntityCode());
        persistenceManagerImpl.jdbcTemplate.execute(new J(this, a, recordId));
    }

    public PersistenceManagerImpl(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource");
        }
        this.metadataManager = new MetadataManagerImpl();
        this.dialect = new MySQLDialect();
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.persistenceManager = this;
        this.sqlCompiler = new SqlCompilerImpl();
        this.queryCache = new QueryCacheImpl(this.persistenceManager);
    }

    @Override
    public OptionCacheManager getOptionCacheManager() {
        return this.queryCache.getOptionCacheManager();
    }

    @Override
    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
    public ID insert(EntityRecord record) {
        return this.jdbcTemplate.execute(new G(this, record));
    }

    @Override
    public QueryCache getQueryCache() {
        return this.queryCache;
    }

    @Override
    public DataQuery createDataQuery() {
        PersistenceManagerImpl persistenceManagerImpl = this;
        return new DataQueryImpl(persistenceManagerImpl, persistenceManagerImpl.queryCache);
    }


    @Override
    public boolean batchAssign(String entityName, String rawFilter, ID newDepartmentId, ID newUserId) {
        SqlHelper.checkSqlInjection(rawFilter);
        Entity entity = this.getMetadataManager().getEntity(entityName);
        if (!entity.isAuthorizable()) {
            return false;
        }
        String entityPhysicalName = entity.getPhysicalName();

        String sql = String.format(" UPDATE `%s` SET ownerDepartment='%s', ownerUser='%s' WHERE %s",
                entityPhysicalName,newDepartmentId.getId(),newUserId.getId(),rawFilter);
        System.err.println( "batchAssign: "+sql);
        this.jdbcTemplate.update(sql);
        return true;
    }

    @Override
    public LicenseInfo validateLicense(String projectName, String account, String secretKey, String licenseCode, Integer port, String offlineLicense) {
        this.licenseInfo = LicenseHelper.getLicense(projectName, account, secretKey, licenseCode, port, offlineLicense);
        return this.licenseInfo;
    }


    @Override
    public boolean batchDelete(String entityName, String rawFilter) {

        SqlHelper.checkSqlInjection(rawFilter);
        Entity entity = this.getMetadataManager().getEntity(entityName);
        String identifier = this.getDialect().getQuotedIdentifier(entity.getPhysicalName());

        String entityPhysicalName = entity.getPhysicalName();

        String a2 = MessageFormat.format("DELETE FROM {0} WHERE {1}", identifier,rawFilter);
        if (SystemEntities.hasDeletedFlag(entity.getName())) {

            a2 = String.format(" UPDATE `%s` SET isDeleted = 1 WHERE %s", entityPhysicalName,rawFilter);
        }
        this.jdbcTemplate.execute(a2);
        return true;
    }
}
