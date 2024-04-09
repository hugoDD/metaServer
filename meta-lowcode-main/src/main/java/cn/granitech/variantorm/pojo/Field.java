package cn.granitech.variantorm.pojo;

import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.serializer.EntitySerializer;
import cn.granitech.variantorm.serializer.EntitySetSerializer;
import cn.granitech.variantorm.serializer.FieldTypeDeserializer;
import cn.granitech.variantorm.serializer.FieldTypeSerializer;
import cn.granitech.variantorm.serializer.IDDeserializer;
import cn.granitech.variantorm.serializer.IDSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import java.util.Set;

@JsonSerialize
public class Field {
    @JsonSerialize(using = IDSerializer.class)
    @JsonDeserialize(using = IDDeserializer.class)
    private ID fieldId;

    private int entityCode;

    private String name;

    private String label;

    private String physicalName;

    @JsonSerialize(using = EntitySerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Entity owner;

    @JsonSerialize(using = FieldTypeSerializer.class)
    @JsonDeserialize(using = FieldTypeDeserializer.class)
    private FieldType type;

    private String description;

    private int displayOrder;

    private boolean nullable;

    private boolean creatable;

    private boolean updatable;

    private boolean idFieldFlag;

    private boolean nameFieldFlag;

    private boolean mainDetailFieldFlag;

    private boolean defaultMemberOfListFlag;

    @JsonSerialize(using = EntitySetSerializer.class)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<Entity> referTo;

    private FieldViewModel fieldViewModel;

    private List<ReferenceModel> referenceSetting;

    public ID getFieldId() {
        return this.fieldId;
    }

    public void setFieldId(ID fieldId) {
        this.fieldId = fieldId;
    }

    public int getEntityCode() {
        return this.entityCode;
    }

    public void setEntityCode(int entityCode) {
        this.entityCode = entityCode;
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

    public Entity getOwner() {
        return this.owner;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public FieldType getType() {
        return this.type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDisplayOrder() {
        return this.displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public boolean isNullable() {
        return this.nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isCreatable() {
        return this.creatable;
    }

    public void setCreatable(boolean creatable) {
        this.creatable = creatable;
    }

    public boolean isUpdatable() {
        return this.updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public boolean isIdFieldFlag() {
        return this.idFieldFlag;
    }

    public void setIdFieldFlag(boolean idFieldFlag) {
        this.idFieldFlag = idFieldFlag;
    }

    public boolean isNameFieldFlag() {
        return this.nameFieldFlag;
    }

    public void setNameFieldFlag(boolean nameFieldFlag) {
        this.nameFieldFlag = nameFieldFlag;
    }

    public boolean isMainDetailFieldFlag() {
        return this.mainDetailFieldFlag;
    }

    public void setMainDetailFieldFlag(boolean mainDetailFieldFlag) {
        this.mainDetailFieldFlag = mainDetailFieldFlag;
    }

    public boolean isDefaultMemberOfListFlag() {
        return this.defaultMemberOfListFlag;
    }

    public void setDefaultMemberOfListFlag(boolean defaultMemberOfListFlag) {
        this.defaultMemberOfListFlag = defaultMemberOfListFlag;
    }

    public Set<Entity> getReferTo() {
        return this.referTo;
    }

    public void setReferTo(Set<Entity> referTo) {
        this.referTo = referTo;
    }

    public FieldViewModel getFieldViewModel() {
        return this.fieldViewModel;
    }

    public void setFieldViewModel(FieldViewModel fieldViewModel) {
        this.fieldViewModel = fieldViewModel;
    }

    public List<ReferenceModel> getReferenceSetting() {
        return this.referenceSetting;
    }

    public void setReferenceSetting(List<ReferenceModel> referenceSetting) {
        this.referenceSetting = referenceSetting;
    }

    public void copyFrom(Field newField) {
        setLabel(newField.getLabel());
        setDescription(newField.getDescription());
        setDisplayOrder(newField.getDisplayOrder());
        setNullable(newField.isNullable());
        setCreatable(newField.isCreatable());
        setUpdatable(newField.isUpdatable());
        setIdFieldFlag(newField.isIdFieldFlag());
        setNameFieldFlag(newField.isNameFieldFlag());
        setMainDetailFieldFlag(newField.isMainDetailFieldFlag());
        setDefaultMemberOfListFlag(newField.isDefaultMemberOfListFlag());
        setReferTo(newField.getReferTo());
        setReferenceSetting(newField.getReferenceSetting());
        setFieldViewModel(newField.getFieldViewModel());
    }
}
