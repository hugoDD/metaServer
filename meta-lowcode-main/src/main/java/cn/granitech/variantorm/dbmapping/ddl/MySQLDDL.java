package cn.granitech.variantorm.dbmapping.ddl;


import cn.granitech.variantorm.dbmapping.ColumnTypeMapping;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.FieldColumnType;
import cn.granitech.variantorm.pojo.FieldViewModel;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class MySQLDDL implements DBDDL {
    private static final String CREATE_FIELD_COLUMN = "ALTER TABLE `%s` ADD COLUMN `%s` %s NULL DEFAULT NULL";
    private static final String DELETE_FIELD_COLUMN = "ALTER TABLE `%s` DROP COLUMN `%s`";
    private DataSource dataSource;
    private static final String DROP_TABLE_SHARE = "DROP TABLE IF EXISTS `%s_share`";
    private static final String FULLCREATE_FIELD_COLUMN = "CREATE TABLE `%s`(autoId INT(11) NOT NULL AUTO_INCREMENT,%s CHAR(40) NOT NULL,`createdOn` DATETIME NOT NULL,`createdBy` CHAR(40) NOT NULL,`ownerUser` CHAR(40) NOT NULL,`ownerDepartment` CHAR(40) NOT NULL,`modifiedOn` DATETIME NULL DEFAULT NULL,`modifiedBy` CHAR(40) NULL DEFAULT NULL,`isDeleted` TINYINT(4) NULL DEFAULT '0',PRIMARY KEY(`autoId`) USING BTREE,UNIQUE INDEX `%s` (`%s`) USING BTREE,INDEX `ownerUser` (`ownerUser`) USING BTREE,INDEX `ownerDepartment` (`ownerDepartment`) USING BTREE,INDEX `isDeleted` (`isDeleted`) USING BTREE )COLLATE='utf8mb4_general_ci'ENGINE=InnoDB;";
    private static final String ALTER_FIELD_COLUMN = "ALTER TABLE `%s` MODIFY COLUMN `%s` %s NULL DEFAULT NULL";
    private static final String CREATE_ENTITY_TABLE = "CREATE TABLE `%s`(autoId INT(11) NOT NULL AUTO_INCREMENT,%s CHAR(40) NOT NULL,`createdOn` DATETIME NOT NULL,`createdBy` CHAR(40) NOT NULL,`modifiedOn` DATETIME NULL DEFAULT NULL,`modifiedBy` CHAR(40) NULL DEFAULT NULL,`isDeleted` TINYINT(4) NULL DEFAULT '0',PRIMARY KEY(`autoId`) USING BTREE,UNIQUE INDEX `%s` (`%s`) USING BTREE,INDEX `isDeleted` (`isDeleted`) USING BTREE )COLLATE='utf8mb4_general_ci'ENGINE=InnoDB;";
    private static final String CREATE_ENTITY_TABLE_SHARE = "  CREATE TABLE `%s_share` (`autoId` INT(11) NOT NULL AUTO_INCREMENT,`recordId` CHAR(40) NOT NULL,`principalId` CHAR(40) NOT NULL,`readable` TINYINT(4) NULL DEFAULT '0',`updatable` TINYINT(4) NULL DEFAULT '0',`deletable` TINYINT(4) NULL DEFAULT '0',`assignable` TINYINT(4) NULL DEFAULT '0',`shareable` TINYINT(4) NULL DEFAULT '0',PRIMARY KEY (`autoId`) USING BTREE,UNIQUE INDEX `recordId_principalId` (`recordId`, `principalId`) USING BTREE,INDEX `recordId_principalId_readable` (`recordId`, `principalId`, `readable`) USING BTREE)COLLATE='utf8mb4_general_ci'ENGINE=InnoDB;";
    private static final String DROP_TABLE = "DROP TABLE `%s`";

    private  String getColumnType(Field field) {
        FieldColumnType fieldColumnType = ColumnTypeMapping.getColumnType(field.getType().getName());
        String columnType = fieldColumnType.getColumnType();

        if (fieldColumnType.hasLength()) {
            columnType = String.format(fieldColumnType.getColumnType(), fieldColumnType.getDefaultLength());
        }
        if (fieldColumnType.hasPrecision()) {

            columnType = String.format(fieldColumnType.getColumnType(), fieldColumnType.getDefaultPrecision());
        }
        return columnType;
    }

    public void deleteFieldColumn(Field field) {
        String ownerPhysicalName = field.getOwner().getPhysicalName();
        String physicalName = field.getPhysicalName();

        SqlHelper.checkSqlInjection(ownerPhysicalName,physicalName);

        String a = String.format(DELETE_FIELD_COLUMN, ownerPhysicalName,physicalName);
       jdbcTemplate.execute(a);
    }

    public void createFieldColumn(Field field) {
        String ownerPhysicalName = field.getOwner().getPhysicalName();
        String physicalName = field.getPhysicalName();
        String type = getColumnType(field);// this.ALLATORIxDEMO(field);

        SqlHelper.checkSqlInjection(ownerPhysicalName,physicalName);

        String a = String.format(CREATE_FIELD_COLUMN, ownerPhysicalName,physicalName,type);
        jdbcTemplate.execute(a);
    }

    public void createEntityTable(Entity entity) {
        String physicalName = entity.getPhysicalName();
        String fieldPhysicalName = entity.getIdField().getPhysicalName();

        SqlHelper.checkSqlInjection(physicalName,fieldPhysicalName);

        String createSql = String.format(CREATE_ENTITY_TABLE, physicalName,fieldPhysicalName,fieldPhysicalName,fieldPhysicalName);
        if (entity.isAuthorizable() || entity.isAssignable()) {

            createSql = String.format(FULLCREATE_FIELD_COLUMN, physicalName,fieldPhysicalName,fieldPhysicalName,fieldPhysicalName);
        }

        jdbcTemplate.execute(createSql);
        if (entity.isShareable()) {
            String shareCreateSql = String.format(CREATE_ENTITY_TABLE_SHARE, physicalName);
            jdbcTemplate.execute(shareCreateSql);
        }

    }

    public void alterFieldColumn(Field field) {
        String ownerPhysicalName = field.getOwner().getPhysicalName();
        String physicalName = field.getPhysicalName();
        String fieldType = getColumnType(field);

        SqlHelper.checkSqlInjection(ownerPhysicalName,physicalName);

        String modifySql = String.format(ALTER_FIELD_COLUMN, ownerPhysicalName,physicalName,fieldType);
        jdbcTemplate.execute(modifySql);
    }

    public void deleteEntityTable(Entity entity) {
        String physicalName = entity.getPhysicalName();

        SqlHelper.checkSqlInjection(physicalName);

        String delSql = String.format(DROP_TABLE, physicalName);
        jdbcTemplate.execute(delSql);

       String  dropShareSql = String.format(DROP_TABLE_SHARE, physicalName);
        jdbcTemplate.execute(dropShareSql);
    }
    private final JdbcTemplate jdbcTemplate;
    public MySQLDDL(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(this.dataSource);
    }
}
