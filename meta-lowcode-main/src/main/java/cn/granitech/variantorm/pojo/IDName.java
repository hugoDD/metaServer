package cn.granitech.variantorm.pojo;

import cn.granitech.variantorm.metadata.ID;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Objects;

@JsonSerialize
public class IDName implements Serializable {
    private ID id;

    private String name;

    public IDName() {}

    public IDName(ID id, String name) {
        this.id = id;
        this.name = name;
    }

    public ID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int hashCode() {
        return Objects.hash(this.id.getId(), this.name);
    }

    public boolean equals(Object obj) {
        if (obj instanceof IDName)
            return ((IDName)obj).id.equals(this.id);
        return false;
    }
}
