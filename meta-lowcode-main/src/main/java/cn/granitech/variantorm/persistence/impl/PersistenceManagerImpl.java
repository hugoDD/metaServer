package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.exception.MetadataSpacesException;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
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
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersistenceManagerImpl implements PersistenceManager {
    private final SqlCompiler sqlCompiler;
    private QueryCache queryCache;
    private LicenseInfo licenseInfo = LicenseInfo.buildFreeLicense("");
    private final Dialect dialect;
    private final MetadataManager metadataManager;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public SqlCompiler getSqlCompiler() {
        return this.sqlCompiler;
    }


    @Override
    public Boolean update(EntityRecord entityRecord) {
        if (entityRecord == null) {
            throw new NullPointerException("record");
        }
        if (entityRecord.id() == null) {
            throw new IllegalArgumentException("Record's id should not be null!");
        }
        if (!entityRecord.isModified()) {
            throw new IllegalArgumentException("Record is unmodified!");
        }

        Collection<Field> fieldSet = entityRecord.getEntity().getFieldSet();
        List<String> updateFields = new ArrayList<>();

        for (Field field : fieldSet) {
            String fieldName = field.getName();
            if (entityRecord.isModified(fieldName) && !field.isNullable() && entityRecord.isNull(fieldName)) {
                throw new IllegalArgumentException("Field '" + field.getLabel() + "(" + fieldName + ")' can't be null!");
            }
            FieldType fieldType = field.getType();
            if (entityRecord.isModified(fieldName) && !fieldType.isVirtual() && fieldType != FieldTypes.PRIMARYKEY) {
                String updateField = getDialect().getQuotedIdentifier(field.getPhysicalName());
                updateFields.add(updateField + "=?");
            }
        }

        if (!updateFields.isEmpty()) {
            String updateSql = "update {0} set {1} where {2} ";
            String identifier = getDialect().getQuotedIdentifier(entityRecord.getEntity().getPhysicalName());
            String updateField = String.join(",", updateFields);
            String idFieldIdentifier = getDialect().getQuotedIdentifier(entityRecord.getEntity().getIdField().getPhysicalName()) + "=?";

            String sql = MessageFormat.format(updateSql, identifier, updateField, idFieldIdentifier);
            int executeUpdate = jdbcTemplate.update(sql, preparedStatement -> {
                int parameterIndex = 1;

                for (Field field : fieldSet) {
                    String fieldName = field.getName();
                    FieldType fieldType = field.getType();
                    if (entityRecord.isModified(fieldName) && !fieldType.isVirtual() && fieldType != FieldTypes.PRIMARYKEY) {
                        Object fieldValue = entityRecord.getFieldValue(fieldName);
                        fieldType.setParamValue(preparedStatement, parameterIndex++, fieldValue, this);
                    }
                }
                ID id = entityRecord.id();
                preparedStatement.setString(parameterIndex, id.toString());
            });

            if (executeUpdate != 1) {
                throw new MetadataSpacesException("Update Record Error: " + entityRecord);
            }
        }

        for (Field field : entityRecord.getEntity().getVirtualFieldSet()) {
            if (entityRecord.isModified(field.getName())) {
                System.err.println("Modified Record: " + field.getName());
                field.getType().setParamValue(this, field, entityRecord.id(), entityRecord.getFieldValue(field.getName()));
            }
        }
        Field field = entityRecord.getEntity().getNameField();
        if (field != null && entityRecord.isModified(field.getName())) {
            IDName idName = new IDName(entityRecord.id(), entityRecord.getName());
            queryCache.updateIDName(idName);
        }

        return true;
    }

    @Override
    public Dialect getDialect() {
        return this.dialect;
    }

    @Override
    public RecordQuery createRecordQuery() {
        return new RecordQueryImpl(this, this.queryCache);
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
    public Boolean delete(ID recordId, String... entityName) {
        Entity entity = this.metadataManager.getEntity(recordId.getEntityCode());
        if (entityName != null && entityName.length > 0 && !entity.getName().equals(entityName[0])) {
            throw new IllegalArgumentException("Mismatch between recordId and entityName!");
        }
        return this.jdbcTemplate.execute(new LogicDeleteCallBack(this, entity, recordId));
    }

    @Override
    public LicenseInfo getLicenseInfo() {
        return this.licenseInfo;
    }

    public void setQueryCache(QueryCache queryCache) {
        this.queryCache = queryCache;
    }


    public PersistenceManagerImpl(JdbcTemplate jdbcTemplate, SqlCompiler sqlCompiler) {
        this.metadataManager = new MetadataManagerImpl();
        this.dialect = new MySQLDialect();
        this.jdbcTemplate = jdbcTemplate;
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
        Entity entity = this.metadataManager.getEntity(recordId.getEntityCode());
        this.jdbcTemplate.execute(new UndeleteCallBack(this, entity, recordId));
    }

    public PersistenceManagerImpl(JdbcTemplate jdbcTemplate) {
        this.metadataManager = new MetadataManagerImpl();
        this.dialect = new MySQLDialect();
        this.jdbcTemplate =jdbcTemplate;
        this.sqlCompiler = new SqlCompilerImpl();
        this.queryCache = new QueryCacheImpl(this);
    }

    @Override
    public OptionCacheManager getOptionCacheManager() {
        return this.queryCache.getOptionCacheManager();
    }



    @Override
    public ID insert(EntityRecord record) {
        return this.jdbcTemplate.execute(new InsertCallBack(this, record));
    }

    @Override
    public QueryCache getQueryCache() {
        return this.queryCache;
    }

    @Override
    public DataQuery createDataQuery() {
        return new DataQueryImpl(this, this.queryCache);
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
                entityPhysicalName, newDepartmentId.getId(), newUserId.getId(), rawFilter);
        System.err.println("batchAssign: " + sql);
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

        String a2 = MessageFormat.format("DELETE FROM {0} WHERE {1}", identifier, rawFilter);
        if (SystemEntities.hasDeletedFlag(entity.getName())) {

            a2 = String.format(" UPDATE `%s` SET isDeleted = 1 WHERE %s", entityPhysicalName, rawFilter);
        }
        this.jdbcTemplate.execute(a2);
        return true;
    }
}
