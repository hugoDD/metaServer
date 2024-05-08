package cn.granitech.business.query;

import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.constant.SystemEntityCodes;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.impl.MetadataManagerImpl;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.util.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                entity.setMetadataManager(metadataManager);
                BeanUtils.copyProperties(v,entity);
                v.getFieldSet().forEach(entity::addField);
                metadataManager.addEntity(entity);
            });
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
    }
}