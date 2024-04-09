package cn.granitech.variantorm.pojo;

import org.springframework.context.ApplicationEvent;

public class ReferenceCacheEO extends ApplicationEvent {
    private String refId;

    private IDName idName;

    public ReferenceCacheEO(Object source) {
        super(source);
    }

    public ReferenceCacheEO(Object source, String refId, IDName idName) {
        super(source);
        this.refId = refId;
        this.idName = idName;
    }

    public String getRefId() {
        return this.refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public IDName getIdName() {
        return this.idName;
    }

    public void setIdName(IDName idName) {
        this.idName = idName;
    }
}
