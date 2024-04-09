package cn.granitech.variantorm.pojo;

import java.util.List;
import org.springframework.context.ApplicationEvent;

public class ReferenceListCacheEO extends ApplicationEvent {
    private String entityName;

    private String refFieldName;

    private String recordId;

    private List<IDName> idNameList;

    public ReferenceListCacheEO(Object source) {
        super(source);
    }

    public ReferenceListCacheEO(Object source, String entityName, String refFieldName) {
        super(source);
        this.entityName = entityName;
        this.refFieldName = refFieldName;
        this.recordId = null;
        this.idNameList = null;
    }

    public ReferenceListCacheEO(Object source, String entityName, String refFieldName, String recordId, List<IDName> idNameList) {
        super(source);
        this.entityName = entityName;
        this.refFieldName = refFieldName;
        this.recordId = recordId;
        this.idNameList = idNameList;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getRefFieldName() {
        return this.refFieldName;
    }

    public void setRefFieldName(String refFieldName) {
        this.refFieldName = refFieldName;
    }

    public String getRecordId() {
        return this.recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public List<IDName> getIdNameList() {
        return this.idNameList;
    }

    public void setIdNameList(List<IDName> idNameList) {
        this.idNameList = idNameList;
    }
}
