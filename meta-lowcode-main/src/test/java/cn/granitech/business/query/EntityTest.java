package cn.granitech.business.query;

import cn.granitech.variantorm.constant.MetaFieldColumns;
import cn.granitech.variantorm.constant.SystemEntityCodes;
import cn.granitech.variantorm.dbmapping.DBMappingHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.metadata.impl.MetadataManagerImpl;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.FieldViewModel;
import cn.granitech.variantorm.pojo.ReferenceModel;
import cn.granitech.variantorm.util.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class EntityTest {

    @Test
    public void jsonToEntity() throws IOException {
        String json = FileUtils.readFileToString(new ClassPathResource("user.json").getFile());
        EntityBo entityBo = JsonUtils.readJsonValue(json, new TypeReference<EntityBo>() {
        });

        System.out.println(JsonUtils.writeObjectAsString(entityBo));
        MetadataManager metadataManager = new MetadataManagerImpl();
        Entity entity = new Entity();
        entity.setMetadataManager(metadataManager);
        BeanUtils.copyProperties(entityBo,entity,"fieldSet");
        entity.setFieldSet(new ArrayList<>());
        entityBo.getFieldSet().stream().map(f -> toField(metadataManager, f))
                .filter(Objects::nonNull)
                .forEach(entity::addField);
        entity.setMetadataManager(null);
        metadataManager.addEntity(entity);

    }

    private static Field toField(MetadataManager mdm, FieldBo rs)  {


        int entityCode = rs.getEntityCode();
        if (!mdm.containsEntity(entityCode)) {
            return null;
        }
        Field field = new Field();
        Entity entity = mdm.getEntity(entityCode);
        field.setOwner(entity);
        field.setFieldId(rs.getFieldId());
        field.setEntityCode(entityCode);
        field.setName(rs.getName());
        field.setLabel(rs.getLabel());
        field.setPhysicalName(rs.getPhysicalName());
        field.setType(rs.getType());
        field.setDescription(rs.getDescription());
        field.setDisplayOrder(rs.getDisplayOrder());
        field.setNullable(rs.isNullable());
        field.setCreatable(rs.isCreatable());
        field.setUpdatable(rs.isUpdatable());
        field.setIdFieldFlag(rs.isIdFieldFlag());
        if (field.isIdFieldFlag()) {
            entity.setIdField(field);
        }
        field.setNameFieldFlag(rs.isNameFieldFlag());
        if (field.isNameFieldFlag()) {
            entity.setNameField(field);
        }
        field.setMainDetailFieldFlag(rs.isMainDetailFieldFlag());
        field.setDefaultMemberOfListFlag(rs.isDefaultMemberOfListFlag());
        List<String> referTo = rs.getReferTo();
        if (referTo!=null && !referTo.isEmpty()) {
            Set<Entity> referToSet = referTo.stream().filter(StringUtils::isNotEmpty)
                    .filter(mdm::containsEntity).map(mdm::getEntity).collect(Collectors.toSet());
            field.setReferTo(referToSet);
        }
        field.setReferenceSetting(rs.getReferenceSetting());
        field.setFieldViewModel(rs.getFieldViewModel());

        entity.addField(field);
        return field;
    }
}
