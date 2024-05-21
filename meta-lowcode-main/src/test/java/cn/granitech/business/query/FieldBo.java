package cn.granitech.business.query;

import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.FieldViewModel;
import cn.granitech.variantorm.pojo.ReferenceModel;
import cn.granitech.variantorm.serializer.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@JsonSerialize
public class FieldBo {
    @JsonSerialize(using = IDSerializer.class)
    @JsonDeserialize(using = IDDeserializer.class)
    private ID fieldId;

    private int entityCode;

    private String name;

    private String label;

    private String physicalName;


    private Map<String,String> owner;

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


    private Set<Map<String,String>> referTo;

    private FieldViewModel fieldViewModel;

    private List<ReferenceModel> referenceSetting;

    public List<String> getReferTo(){
        if(referTo == null){
            return null;
        }
      return   referTo.stream().map(m->m.get("name")).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Field{" +
                "fieldId=" + fieldId +
                ", entityCode=" + entityCode +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", physicalName='" + physicalName + '\'' +
                ", owner=" + owner +
                ", type=" + type +
                ", description='" + description + '\'' +
                ", displayOrder=" + displayOrder +
                ", nullable=" + nullable +
                ", creatable=" + creatable +
                ", updatable=" + updatable +
                ", idFieldFlag=" + idFieldFlag +
                ", nameFieldFlag=" + nameFieldFlag +
                ", mainDetailFieldFlag=" + mainDetailFieldFlag +
                ", defaultMemberOfListFlag=" + defaultMemberOfListFlag +
                ", referTo=" + referTo +
                ", fieldViewModel=" + fieldViewModel +
                ", referenceSetting=" + referenceSetting +
                '}';
    }
}
