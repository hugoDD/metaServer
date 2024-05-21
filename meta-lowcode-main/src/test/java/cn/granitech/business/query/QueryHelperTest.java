package cn.granitech.business.query;

import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.constant.SystemEntityCodes;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.impl.MetadataManagerImpl;
import cn.granitech.variantorm.persistence.impl.EasySQLHelper;
import cn.granitech.variantorm.persistence.queryCompiler.SelectStatement;
import cn.granitech.variantorm.persistence.queryCompiler.SqlCompiler;
import cn.granitech.variantorm.persistence.queryCompiler.SqlCompilerImpl;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.variantorm.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class QueryHelperTest {
    MetadataManager metadataManager = new MetadataManagerImpl();

    @BeforeEach
    void setUp() {
        try (InputStream inputStream = (new ClassPathResource("entitys.json")).getInputStream()) {
            String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Map<String,EntityBo> stringEntityBoMap = JsonUtils.readJsonValue(json, new TypeReference<Map<String,EntityBo>>() {
            });
            System.out.println(stringEntityBoMap.values().size());
            stringEntityBoMap.forEach((k,v)->{
                Entity entity = new Entity();
                BeanUtils.copyProperties(v,entity,"fieldSet");
                metadataManager.addEntity(entity);
            });
            stringEntityBoMap.values().stream()
                    .flatMap(e->e.getFieldSet().stream())
                    .forEach(m->toField(metadataManager,m));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void compileEasySql() {
        Entity entity = metadataManager.getEntity(SystemEntities.User);
        assertNotNull(entity);
        assertEquals(entity.getEntityCode(), SystemEntityCodes.USER_CODE);
        Entity userEntity = metadataManager.getEntity(SystemEntityCodes.USER_CODE);
        assertSame(entity, userEntity);
        SqlCompiler sqlCompiler = new SqlCompilerImpl();
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity("User");
        querySchema.setFilter(" [departmentId.departmentName] like '%公司%' ");
        List<String> nameList = entity.getFieldSet()
                .stream().map(Field::getName).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        querySchema.setSelectFields(String.join(",",nameList));

        String sql = EasySQLHelper.generateSql(querySchema, null);
        System.out.println(sql);
        SelectStatement selectStatement = sqlCompiler.compileEasySql(metadataManager, sql);
        System.out.println(selectStatement);
    }

    @Test
    void singleUserSql(){
        Entity entity = metadataManager.getEntity(SystemEntities.User);
        SqlCompiler sqlCompiler = new QueryHelper();
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity("User");

        querySchema.setFilter(" userName like pn_userName ");
        List<String> nameList = entity.getFieldSet()
                .stream().map(Field::getName).filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        querySchema.setSelectFields("userId,userName");
        Pagination pagination = new Pagination(1,20);
        String sql = EasySQLHelper.generateSql(querySchema, pagination);
        System.out.println(sql);
        SelectStatement selectStatement = sqlCompiler.compileEasySql(metadataManager, sql);
        System.out.println(selectStatement);
    }

    private static Field toField(MetadataManager mdm, FieldBo fieldBo)  {
        int entityCode = fieldBo.getEntityCode();

        Field field = new Field();
        Entity entity = mdm.getEntity(entityCode);
        field.setOwner(entity);
        field.setFieldId(fieldBo.getFieldId());
        field.setEntityCode(entityCode);
        field.setName(fieldBo.getName());
        field.setLabel(fieldBo.getLabel());
        field.setPhysicalName(fieldBo.getPhysicalName());
        field.setType(fieldBo.getType());
        field.setDescription(fieldBo.getDescription());
        field.setDisplayOrder(fieldBo.getDisplayOrder());
        field.setNullable(fieldBo.isNullable());
        field.setCreatable(fieldBo.isCreatable());
        field.setUpdatable(fieldBo.isUpdatable());
        field.setIdFieldFlag(fieldBo.isIdFieldFlag());
        if (field.isIdFieldFlag()) {
            entity.setIdField(field);
        }
        field.setNameFieldFlag(fieldBo.isNameFieldFlag());
        if (field.isNameFieldFlag()) {
            entity.setNameField(field);
        }
        field.setMainDetailFieldFlag(fieldBo.isMainDetailFieldFlag());
        field.setDefaultMemberOfListFlag(fieldBo.isDefaultMemberOfListFlag());
        List<String> referTo = fieldBo.getReferTo();
        if (referTo!=null && !referTo.isEmpty()) {
            Set<Entity> referToSet = referTo.stream().filter(StringUtils::isNotEmpty)
                    .filter(mdm::containsEntity).map(mdm::getEntity).collect(Collectors.toSet());
            field.setReferTo(referToSet);
        }
        field.setReferenceSetting(fieldBo.getReferenceSetting());
        field.setFieldViewModel(fieldBo.getFieldViewModel());

        entity.addField(field);
        return field;
    }
}