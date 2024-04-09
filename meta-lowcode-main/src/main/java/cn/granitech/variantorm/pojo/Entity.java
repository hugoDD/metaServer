package cn.granitech.variantorm.pojo;

import cn.granitech.variantorm.constant.CommonFields;
import cn.granitech.variantorm.exception.InvalidFieldException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.serializer.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.*;

@JsonSerialize
public class Entity {
    @JsonSerialize(
            using = IDSerializer.class
    )
    @JsonDeserialize(
            using = IDDeserializer.class
    )
    private ID entityId;
    private String name;
    private String label;
    private String physicalName;
    private Integer entityCode;
    private boolean detailEntityFlag;
    private boolean layoutable;
    private boolean listable;
    private boolean authorizable;
    private boolean shareable;
    private boolean assignable;
    private String tags;
    @JsonSerialize(
            using = FieldSerializer.class
    )
    @JsonProperty(
            access = Access.READ_ONLY
    )
    private Field idField;
    @JsonSerialize(
            using = FieldSerializer.class
    )
    @JsonProperty(
            access = Access.READ_ONLY
    )
    private Field nameField;
    @JsonSerialize(
            using = EntitySetSerializer.class
    )
    @JsonProperty(
            access = Access.READ_ONLY
    )
    private Set<Entity> detailEntitySet = new HashSet<>();
    @JsonSerialize(
            using = EntitySerializer.class
    )
    @JsonProperty(
            access = Access.READ_ONLY
    )
    private Entity mainEntity;
    @JsonIgnore
    private MetadataManager metadataManager;
    @JsonIgnore
    private final Map<String, Field> fields = new LinkedHashMap<>();

    public Entity() {
    }

    public ID getEntityId() {
        return this.entityId;
    }

    public void setEntityId(ID entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPhysicalName() {
        return this.physicalName;
    }

    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    public Integer getEntityCode() {
        return this.entityCode;
    }

    public void setEntityCode(Integer entityCode) {
        this.entityCode = entityCode;
    }

    public boolean isDetailEntityFlag() {
        return this.detailEntityFlag;
    }

    public void setDetailEntityFlag(boolean detailEntityFlag) {
        this.detailEntityFlag = detailEntityFlag;
    }

    public boolean isLayoutable() {
        return this.layoutable;
    }

    public void setLayoutable(boolean layoutable) {
        this.layoutable = layoutable;
    }

    public boolean isListable() {
        return this.listable;
    }

    public void setListable(boolean listable) {
        this.listable = listable;
    }

    public boolean isAuthorizable() {
        return this.authorizable;
    }

    public void setAuthorizable(boolean authorizable) {
        this.authorizable = authorizable;
    }

    public boolean isShareable() {
        return this.shareable;
    }

    public void setShareable(boolean shareable) {
        this.shareable = shareable;
    }

    public boolean isAssignable() {
        return this.assignable;
    }

    public void setAssignable(boolean assignable) {
        this.assignable = assignable;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Field getIdField() {
        return this.idField;
    }

    public void setIdField(Field idField) {
        this.idField = idField;
        idField.setOwner(this);
    }

    public Field getNameField() {
        return this.nameField;
    }

    public void setNameField(Field nameField) {
        this.nameField = nameField;
        nameField.setNameFieldFlag(true);
    }

    public Collection<Field> getFieldSet() {
        return Collections.unmodifiableCollection(this.fields.values());
    }

    public Collection<Field> getSortedFieldSet() {
        List<Field> sortedFieldSet = new ArrayList<>(this.fields.values());
        Collections.copy(sortedFieldSet, new ArrayList<>(this.fields.values()));
        sortedFieldSet.sort((o1, o2) -> {
            if (CommonFields.containField(o1.getName()) && !CommonFields.containField(o2.getName())) {
                return 1;
            } else {
                return !CommonFields.containField(o1.getName()) && CommonFields.containField(o2.getName()) ? -1 : 0;
            }
        });
        return sortedFieldSet;
    }

    public Set<String> getFieldNames() {
        return Collections.unmodifiableSet(this.fields.keySet());
    }

    public void setFieldSet(Collection<Field> fieldSet) {
        this.fields.clear();

        for (Field field : fieldSet) {
            this.fields.put(field.getName(), field);
        }

    }

    public Collection<Field> getVirtualFieldSet() {
        Collection<Field> virtualFieldSet = new HashSet<>();

        for (Field field : this.fields.values()) {
            if (field.getType().isVirtual()) {
                virtualFieldSet.add(field);
            }
        }

        return Collections.unmodifiableCollection(virtualFieldSet);
    }

    public Set<Entity> getDetailEntitySet() {
        return this.detailEntitySet;
    }

    public void setDetailEntitySet(Set<Entity> detailEntitySet) {
        this.detailEntitySet = detailEntitySet;
    }

    public boolean hasDetailEntity() {
        for (Entity detailEntity : this.detailEntitySet) {
            if (this.metadataManager.containsEntity(detailEntity.getEntityCode()))
                return true;
        }
        return false;
    }

    public Entity getMainEntity() {
        return this.mainEntity;
    }

    public void setMainEntity(Entity mainEntity) {
        this.mainEntity = mainEntity;
    }

    public MetadataManager getMetadataManager() {
        return this.metadataManager;
    }

    public void setMetadataManager(MetadataManager mdm) {
        this.metadataManager = mdm;
    }

    public boolean containsField(String fieldName) {
        return this.fields.containsKey(fieldName);
    }

    public Field getMainDetailField() {
        if (!this.detailEntityFlag)
            return null;
        for (Field field : getFieldSet()) {
            if (field.isMainDetailFieldFlag())
                return field;
        }
        return null;
    }

    public Field getField(String fieldName) throws InvalidFieldException {
        Field result = this.fields.get(fieldName);
        if (result == null) {
            throw new InvalidFieldException("No such field: [" + fieldName + "] in entity: " + this.getName());
        } else {
            return result;
        }
    }

    public boolean addDetailEntity(String detailEntityName) {
        Entity detailEntity = this.metadataManager.getEntity(detailEntityName);
        this.detailEntitySet.add(detailEntity);
        detailEntity.setMainEntity(this);
        return true;
    }

    public boolean addField(Field field) {
        if (this.fields.containsKey(field.getName())) {
            throw new IllegalArgumentException("Duplicated field: " + field.getName());
        } else {
            field.setOwner(this);
            field.setEntityCode(this.entityCode);
            this.fields.put(field.getName(), field);
            return true;
        }
    }

    public boolean updateField(String fieldName, Field field) {
        Field savedField = this.getField(fieldName);
        savedField.copyFrom(field);
        return true;
    }

    public boolean removeField(String fieldName) {
        Field field = this.fields.remove(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("field not exists: " + fieldName);
        } else {
            field.setOwner(null);

            return true;
        }
    }
}
