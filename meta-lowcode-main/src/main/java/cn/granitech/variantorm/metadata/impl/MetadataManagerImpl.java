package cn.granitech.variantorm.metadata.impl;


import cn.granitech.variantorm.exception.InvalidEntityException;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MetadataManagerImpl implements MetadataManager {
    private final Map<Integer, Entity> entityCodeMap = new HashMap<>();
    private final Map<String, Entity> entityNameMap = new HashMap<>();

    public boolean containsEntity(int entityCode) {
        if (entityCode <= 0) {
            throw new IllegalArgumentException("Entity code must be greater than 0.");
        } else {
            return this.entityCodeMap.containsKey(entityCode);
        }
    }

    public boolean containsEntity(String entityName) {
        return this.entityNameMap.containsKey(entityName);
    }

    public List<String> getAllTagsOfEntity() {
        List<String> list = new ArrayList<>();
        this.entityNameMap.forEach((key, entity) -> {
            if (StringUtils.isNotBlank(entity.getTags())) {
               Stream.of(entity.getTags().split(","))
                        .filter(StringUtils::isNotBlank)
                        .map(String::trim).forEach(list::add);

            }

        });
        return list;
    }

    public boolean removeEntity(String entityName) {
        Entity entity =this.entityNameMap.remove(entityName);
        if (entity == null) {
            throw new IllegalArgumentException("Invalid entity name or entity not exists: "+entityName);
        } else {
            if (entity.getMainEntity() != null) {
                Set<Entity> detailEntitySet = entity.getMainEntity().getDetailEntitySet();
                Set<Entity> set = detailEntitySet.stream().filter(e->!Objects.equals(e.getName(),entityName))
                        .collect(Collectors.toSet());

                entity.getMainEntity().setDetailEntitySet(set);
            }

            this.entityCodeMap.remove(entity.getEntityCode());
            entity.setMetadataManager(null);
            return true;
        }
    }

    public Collection<Entity> getEntitySet() {
        List<Entity> list = new ArrayList<>(this.entityNameMap.values());
        list.sort(Comparator.comparingInt(Entity::getEntityCode));
        return list;
    }

    public Entity getEntity(int entityCode) throws InvalidEntityException {
        if (entityCode <= 0) {
            throw new IllegalArgumentException("Entity code must be greater than 0.");
        } else {
            Entity a;
            if ((a = this.entityCodeMap.get(entityCode)) == null) {
                throw new InvalidEntityException("No such entity code:" +entityCode);
            } else {
                return a;
            }
        }
    }

    public boolean updateField(String entityName, String fieldName, Field field) {
        Entity a;
        if ((a = this.entityNameMap.get(entityName)) == null) {
            throw new IllegalArgumentException("Invalid entity name or entity not exists: "+entityName);
        } else {
            return a.updateField(fieldName, field);
        }
    }

    public boolean removeField(String entityName, String fieldName) {
        Entity a;
        if ((a = this.entityNameMap.get(entityName)) == null) {
            throw new IllegalArgumentException("Invalid entity name or entity not exists: "+entityName);
        } else {
            return a.removeField(fieldName);
        }
    }

    public boolean addField(String entityName, Field field) {
        Entity entity=this.entityNameMap.get(entityName);
        if (entity == null) {
            throw new IllegalArgumentException("Invalid entity name or entity not exists: "+entityName);
        } else {
            return entity.addField(field);
        }
    }

    public boolean removeField(Integer entityCode, String fieldName) {
        Entity a;
        if ((a = this.entityCodeMap.get(entityCode)) == null) {
            throw new IllegalArgumentException("Invalid entity code or entity not exists: "+entityCode);
        } else {
            return a.removeField(fieldName);
        }
    }

    public Entity getEntity(String entityName) throws InvalidEntityException {
        if (this.entityNameMap.get(entityName) == null) {
            throw new InvalidEntityException( "No such entity name: "+entityName);
        } else {
            return this.entityNameMap.get(entityName);
        }
    }

    public boolean addEntity(Entity newEntity) {
        if (newEntity.getMetadataManager() != null) {
            throw new IllegalArgumentException("Entity ["+newEntity.getName()+"] was owned by other MetaDataManager.");
        } else {
            int entityCode = newEntity.getEntityCode();
            if (entityCode <= 0) {
                throw new IllegalArgumentException( "Invalid entity code: "+entityCode);
            } else {
                Iterator<String> var3 = this.entityNameMap.keySet().iterator();

                do {
                    if (!var3.hasNext()) {
                        if (this.entityCodeMap.containsKey(entityCode)) {
                            throw new IllegalArgumentException("Duplicated entity type code: entity="+newEntity.getName()+", typeCode="+newEntity.getEntityCode());
                        }

                        newEntity.setMetadataManager(this);
                        this.entityNameMap.put(newEntity.getName(), newEntity);
                        this.entityCodeMap.put(entityCode, newEntity);
                        if (newEntity.getMainEntity() != null && newEntity.isDetailEntityFlag()) {
                            newEntity.getMainEntity().addDetailEntity(newEntity.getName());
                        }

                        return true;
                    }
                } while(!var3.next().equalsIgnoreCase(newEntity.getName()));

                throw new IllegalArgumentException("Duplicated entity type code: entity="+newEntity.getName()+", typeCode="+newEntity.getEntityCode());
            }
        }
    }

    public boolean updateEntity(Entity newEntity) {
        newEntity.setMetadataManager(this);
        this.entityNameMap.put(newEntity.getName(), newEntity);
        return true;
    }
}
