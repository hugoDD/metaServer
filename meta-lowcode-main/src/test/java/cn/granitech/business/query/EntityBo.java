package cn.granitech.business.query;

import cn.granitech.variantorm.constant.CommonFields;
import cn.granitech.variantorm.exception.InvalidFieldException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.serializer.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.*;

@Data
@JsonSerialize
public class EntityBo {
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
    private Field idField;
    @JsonSerialize(
            using = FieldSerializer.class
    )
    private Field nameField;

    @JsonSerialize(
            using = EntitySetSerializer.class
    )
    private Set<EntityBo> detailEntitySet = new HashSet<>();

    @JsonSerialize(
            using = EntitySerializer.class
    )
    private EntityBo mainEntity;

    private final List<FieldBo> fieldSet = new ArrayList<>();

}
