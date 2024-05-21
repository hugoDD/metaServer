package cn.granitech.variantorm.dbmapping;


import cn.granitech.variantorm.constant.MetaEntityColumns;
import cn.granitech.variantorm.constant.MetaFieldColumns;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.FieldViewModel;
import cn.granitech.variantorm.pojo.ReferenceModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cn.granitech.variantorm.constant.MetaEntityColumns.entityId;
import static cn.granitech.variantorm.constant.MetaEntityColumns.label;

public class DBMappingHelper {
    private static final String SPLIT_STR = ",";

    public static void updateMetaEntityRecord(JdbcTemplate jdbcTemplate, Entity entity) {
        String sql = "UPDATE `t_meta_entity` SET `label`=?, `layoutable`=?,  `listable`=?, `tags`=? WHERE `name`=? ";


        jdbcTemplate.update(sql, entity.getLabel(), entity.isLayoutable(), entity.isListable(), entity.getTags(), entity.getName());
    }

    public static boolean checkFieldExists(JdbcTemplate jdbcTemplate, int entityCode, String fieldName) {

        String sql = " SELECT count(*) FROM `t_meta_field` where `entityCode`=? AND `name`=? ";

        Integer fieldCount = jdbcTemplate.queryForObject(sql, Integer.class, entityCode, fieldName);
        return fieldCount != null && fieldCount > 0;
    }

    public static void insertMetaEntityRecord(JdbcTemplate jdbcTemplate, Entity entity) {
        String sql = "INSERT INTO `t_meta_entity` (`entityId`,`name`,`label`,`physicalName`,`entityCode`,  `detailEntityFlag`, `mainEntity`, `layoutable`, `listable`, `authorizable`, `shareable`, `assignable`, `tags`)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) \n";
        ID newID = ID.newID(1);
        int entityCode = entity.getEntityCode() == null ? newID.getEntityCode() : entity.getEntityCode();
        String mainEntityName = entity.getMainEntity() == null ? null : entity.getMainEntity().getName();

        Object[] param = new Object[13];
        param[0] = newID;
        param[1] = entity.getName();
        param[2] = entity.getLabel();
        param[3] = entity.getPhysicalName();
        param[4] = entityCode;
        param[5] = entity.isDetailEntityFlag();
        param[6] = mainEntityName;
        param[7] = entity.isLayoutable();
        param[8] = entity.isListable();
        param[9] = entity.isAuthorizable();
        param[10] = entity.isShareable();
        param[11] = entity.isAssignable();
        param[12] = entity.getTags();
        jdbcTemplate.update(sql, param);
        entity.setEntityCode(entityCode);
    }

    private static /* synthetic */ int ALLATORIxDEMO(JdbcTemplate jdbcTemplate) {
        Integer a = jdbcTemplate.queryForObject(" SELECT MAX(`entityCode`) FROM `t_meta_entity` ", Integer.class);
        if (a == null || a <= 1000) {
            return 1001;
        }
        return a + 1;
    }

    public static void deleteMetaFieldRecord(JdbcTemplate jdbcTemplate, int entityCode, String fieldName) {

        String sql = " DELETE FROM `t_meta_field` WHERE `entityCode`=? AND `name`=? ";

        jdbcTemplate.update(sql, entityCode, fieldName);
    }

    /*
     * WARNING - void declaration
     */
    private static  Field toField(MetadataManager mdm, ResultSet rs) throws SQLException {


        int entityCode = rs.getInt(MetaFieldColumns.entityCode);
        if (!mdm.containsEntity(entityCode)) {
            return null;
        }
        Field field = new Field();
        Entity entity = mdm.getEntity(entityCode);
        field.setOwner(entity);
        String fieldId = rs.getString(MetaFieldColumns.fieldId);
        field.setFieldId(new ID(fieldId));
        field.setEntityCode(entityCode);
        field.setName(rs.getString(MetaFieldColumns.name));
        field.setLabel(rs.getString(MetaFieldColumns.label));
        field.setPhysicalName(rs.getString(MetaFieldColumns.physicalName));
        field.setType(FieldTypes.getType(rs.getString(MetaFieldColumns.type)));
        field.setDescription(rs.getString(MetaFieldColumns.description));
        field.setDisplayOrder(rs.getInt(MetaFieldColumns.displayOrder));
        field.setNullable(rs.getInt(MetaFieldColumns.nullable) != 0);
        field.setCreatable(rs.getInt(MetaFieldColumns.creatable) != 0);
        field.setUpdatable(rs.getInt(MetaFieldColumns.updatable) != 0);
        field.setIdFieldFlag(rs.getInt(MetaFieldColumns.idFieldFlag) != 0);
        if (field.isIdFieldFlag()) {
            entity.setIdField(field);
        }
        field.setNameFieldFlag(rs.getInt(MetaFieldColumns.nameFieldFlag) != 0);
        if (field.isNameFieldFlag()) {
            entity.setNameField(field);
        }
        field.setMainDetailFieldFlag(rs.getInt(MetaFieldColumns.mainDetailFieldFlag) != 0);
        field.setDefaultMemberOfListFlag(rs.getInt(MetaFieldColumns.defaultMemberOfListFlag) != 0);
        String referTo = rs.getString(MetaFieldColumns.referTo);
        if (StringUtils.isNotEmpty(referTo)) {
            Set<Entity> referToSet = Stream.of(referTo.split(SPLIT_STR)).filter(StringUtils::isNotEmpty)
                    .filter(mdm::containsEntity).map(mdm::getEntity).collect(Collectors.toSet());
            field.setReferTo(referToSet);
        }
        String referenceSetting = rs.getString(MetaFieldColumns.referenceSetting);
        if (StringUtils.isNotBlank(referenceSetting) && !"null".equalsIgnoreCase(referenceSetting)) {
            try {
                field.setReferenceSetting(DBMappingHelper.readValue(referenceSetting, new TypeReference<List<ReferenceModel>>() {
                }));
            } catch (JsonProcessingException a9) {
                a9.printStackTrace();
            }
        }

        String fieldViewModel = rs.getString(MetaFieldColumns.fieldViewModel);
        if (StringUtils.isNotBlank(fieldViewModel) && !"null".equalsIgnoreCase(fieldViewModel)) {
            try {
                field.setFieldViewModel(DBMappingHelper.readValue(fieldViewModel, new TypeReference<FieldViewModel>() {
                }));
            } catch (JsonProcessingException a10) {
                a10.printStackTrace();
            }
        }
        entity.addField(field);
        return field;
    }


    private static Entity toEntity(MetadataManager mdm, ResultSet rs) throws SQLException {
        Entity entity = new Entity();
        ID id = new ID(rs.getString(entityId));
        entity.setEntityId(id);
        entity.setName(rs.getString(MetaEntityColumns.name));
        entity.setLabel(rs.getString(label));
        entity.setPhysicalName(rs.getString(MetaEntityColumns.physicalName));
        entity.setEntityCode(rs.getInt(MetaEntityColumns.entityCode));
        entity.setDetailEntityFlag(rs.getInt(MetaEntityColumns.detailEntityFlag) != 0);
        if (entity.isDetailEntityFlag()) {
            String mainEntity = rs.getString(MetaEntityColumns.mainEntity);
            entity.setMainEntity(mdm.getEntity(mainEntity));
        } else {
            entity.setDetailEntitySet(new LinkedHashSet<>());
        }
        entity.setLayoutable(rs.getInt(MetaEntityColumns.layoutable) != 0);
        entity.setListable(rs.getInt(MetaEntityColumns.listable) != 0);
        entity.setAuthorizable(rs.getInt(MetaEntityColumns.authorizable) != 0);
        entity.setShareable(rs.getInt(MetaEntityColumns.shareable) != 0);
        entity.setAssignable(rs.getInt(MetaEntityColumns.assignable) != 0);
        entity.setTags(rs.getString(MetaEntityColumns.tags));
        return entity;
    }

    public static void updateMetaFieldRecord(JdbcTemplate jdbcTemplate, int entityCode, Field field) {
        String sql = "UPDATE `t_meta_field` SET `label`=?, `description`=?, `displayOrder`=?, `nullable`=?,  `creatable`=?, `updatable`=?, `idFieldFlag`=?, `nameFieldFlag`=?, `mainDetailFieldFlag`=?,  `defaultMemberOfListFlag`=?, `referTo`=?, `referenceSetting`=?, `fieldViewModel`=?  WHERE `entityCode`=? AND `name`=?";
        String referName = "";
        if (field.getReferTo() != null) {
            referName = field.getReferTo().stream().map(Entity::getName).collect(Collectors.joining(","));

        }
        ObjectMapper mapper = new ObjectMapper();
        String fieldViewModel = null;
        String referenceSetting = null;

        try {
            if (field.getFieldViewModel() != null) {
                fieldViewModel = mapper.writeValueAsString(field.getFieldViewModel());
            }

            if (StringUtils.isBlank(fieldViewModel) || "null".equalsIgnoreCase(fieldViewModel)) {
                fieldViewModel = null;
            }

            if (field.getReferenceSetting() != null) {
                referenceSetting = mapper.writeValueAsString(field.getReferenceSetting());
            }

            if (StringUtils.isBlank(referenceSetting) || "null".equalsIgnoreCase(referenceSetting)) {
                referenceSetting = null;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        Object[] paraArray = new Object[15];
        paraArray[0] = field.getLabel();
        paraArray[1] = field.getDescription();
        paraArray[2] = field.getDisplayOrder();
        paraArray[3] = field.isNullable();
        paraArray[4] = field.isCreatable();
        paraArray[5] = field.isUpdatable();
        paraArray[6] = field.isIdFieldFlag();
        paraArray[7] = field.isNameFieldFlag();
        paraArray[8] = field.isMainDetailFieldFlag();
        paraArray[9] = field.isDefaultMemberOfListFlag();
        paraArray[10] = referName;
        paraArray[11] = fieldViewModel;
        paraArray[12] = referenceSetting;
        paraArray[13] = entityCode;
        paraArray[14] = field.getName();
        jdbcTemplate.update(sql, paraArray);
    }

    public static void setNameFieldOfEntity(JdbcTemplate jdbcTemplate, Entity entity, Field field) {

        String updateSql = " UPDATE `t_meta_field` SET `nameFieldFlag`=0 WHERE `entityCode`=? ";

        jdbcTemplate.update(updateSql, entity.getEntityCode());
        String sql = " UPDATE `t_meta_field` SET `nameFieldFlag`=1 WHERE `entityCode`=? AND `name`=? ";

        jdbcTemplate.update(sql, entity.getEntityCode(), field.getName());
    }

    public static void insertMetaFieldRecord(JdbcTemplate jdbcTemplate, int entityCode, Field field) {
        String sql = "INSERT INTO `t_meta_field` (`fieldId`, `entityCode`,`name`,`label`,`physicalName`,`type`,  `description`, `displayOrder`, `nullable`, `creatable`, `updatable`, `idFieldFlag`, `nameFieldFlag`,  `mainDetailFieldFlag`, `defaultMemberOfListFlag`, `referTo`, `referenceSetting`, `fieldViewModel`)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        ID newID = ID.newID(2);
        String referName = "";
        if (field.getReferTo() != null) {
            referName = field.getReferTo().stream().map(Entity::getName).collect(Collectors.joining(","));

        }
        ObjectMapper mapper = new ObjectMapper();
        String fieldViewModel = null;
        String referenceSetting = null;

        try {
            if (field.getFieldViewModel() != null) {
                fieldViewModel = mapper.writeValueAsString(field.getFieldViewModel());
            }

            if (StringUtils.isBlank(fieldViewModel) || "null".equalsIgnoreCase(fieldViewModel)) {
                fieldViewModel = null;
            }

            if (field.getReferenceSetting() != null) {
                referenceSetting = mapper.writeValueAsString(field.getReferenceSetting());
            }

            if (StringUtils.isBlank(referenceSetting) || "null".equalsIgnoreCase(referenceSetting)) {
                referenceSetting = null;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        Object[] paraArray = new Object[18];
        paraArray[0] = newID.getId();
        paraArray[1] = entityCode;
        paraArray[2] = field.getName();
        paraArray[3] = field.getLabel();
        paraArray[4] = field.getPhysicalName();
        paraArray[5] = field.getType().getName();
        paraArray[6] = field.getDescription();
        paraArray[7] = field.getDisplayOrder();
        paraArray[8] = field.isNullable();
        paraArray[9] = field.isCreatable();
        paraArray[10] = field.isUpdatable();
        paraArray[11] = field.isIdFieldFlag();
        paraArray[12] = field.isNameFieldFlag();
        paraArray[13] = field.isMainDetailFieldFlag();
        paraArray[14] = field.isDefaultMemberOfListFlag();
        paraArray[15] = referName;
        paraArray[16] = referenceSetting;
        paraArray[17] = fieldViewModel;
        jdbcTemplate.update(sql, paraArray);
    }

    public static void loadMetadataFromDB(JdbcTemplate jdbcTemplate, MetadataManager mdm) {
        jdbcTemplate.query(" SELECT * FROM `t_meta_entity` order by entityCode ", resultSet -> {
            Entity entity = toEntity(mdm, resultSet);
            mdm.addEntity(entity);
        });
        Collection<Entity> entitySet = mdm.getEntitySet();
        for (Entity entity : entitySet) {
            if (entity.isDetailEntityFlag()) {
                continue;
            }
            entity.setMainEntity(null);
        }
        for (Entity entity : entitySet) {
            Entity mainEntity= entity.getMainEntity();
            if (!entity.isDetailEntityFlag() || mainEntity  == null){
                continue;
            }
            Set<Entity> detailEntitySet = mainEntity.getDetailEntitySet() != null ? mainEntity.getDetailEntitySet() : new LinkedHashSet<>();
            detailEntitySet.add(entity);
        }
        jdbcTemplate.query(" SELECT * FROM `t_meta_field` ", resultSet->{
            toField(mdm, resultSet);
        });
    }

    public static void deleteMetaEntityRecord(JdbcTemplate jdbcTemplate, String entityName, int entityCode) {
        String entitySql = " DELETE FROM `t_meta_entity` WHERE `name`=? ";

        jdbcTemplate.update(entitySql, entityName);
        String fieldSql = " DELETE FROM `t_meta_field` WHERE `entityCode`=? ";

        jdbcTemplate.update(fieldSql, entityCode);
    }


    private static <T> T readValue(String content, TypeReference<T> valueTypeRef) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(content, valueTypeRef);
    }

    public static boolean checkEntityExists(JdbcTemplate jdbcTemplate, String entityName) {

        String sql = " SELECT count(*) FROM `t_meta_entity` where `name`=? ";
        Integer entityCount = jdbcTemplate.queryForObject(sql, Integer.class, entityName);
        return entityCount!=null && entityCount > 0;
    }

    public static boolean checkTableExists(JdbcTemplate jdbcTemplate, String tableName) {

        String sqlStr = " SHOW TABLES LIKE '%s' ";
        String sql = String.format(sqlStr, tableName);
        List<String> list = jdbcTemplate.queryForList(sql, String.class);
        return list.size() > 0;
    }
}


