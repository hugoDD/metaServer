package cn.granitech.web.pojo.approval.node;

import cn.granitech.variantorm.metadata.ID;

import java.io.Serializable;

public class NodeRole implements Serializable {
    private ID id;
    private String name;

    public ID getId() {
        return this.id;
    }

    public void setId(ID id2) {
        this.id = id2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
