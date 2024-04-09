package cn.granitech.variantorm.metadata;


import cn.granitech.variantorm.exception.InvalidEntityException;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;

import java.util.Collection;
import java.util.List;

public interface MetadataManager {
    List<String> getAllTagsOfEntity();

    boolean containsEntity(int entityCode);

    Collection<Entity> getEntitySet();

    boolean removeField(Integer entityCode, String fieldName);

    boolean removeEntity(String entityName);

    boolean addEntity(Entity newEntity);

    boolean updateEntity(Entity newEntity);

    boolean removeField(String entityName, String fieldName);

    Entity getEntity(String entityName) throws InvalidEntityException;

    boolean updateField(String entityName, String fieldName, Field field);

    boolean containsEntity(String entityName);

    Entity getEntity(int entityCode) throws InvalidEntityException;

    boolean addField(String entityName, Field field);
}
