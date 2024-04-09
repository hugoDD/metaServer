package cn.granitech.variantorm.pojo;

public class FieldColumnType {
    private String columnType;

    private Boolean lengthFlag;

    private Boolean precisionFlag;

    private Integer defaultLength;

    private Integer defaultPrecision;

    public FieldColumnType(String columnType) {
        this.columnType = columnType;
        this.lengthFlag = Boolean.FALSE;
        this.precisionFlag = Boolean.FALSE;
        this.defaultLength = null;
        this.defaultPrecision = null;
    }

    public FieldColumnType(String columnType, Boolean lengthFlag, Boolean precisionFlag, Integer defaultLength, Integer defaultPrecision) {
        this.columnType = columnType;
        this.lengthFlag = lengthFlag;
        this.precisionFlag = precisionFlag;
        this.defaultLength = defaultLength;
        this.defaultPrecision = defaultPrecision;
    }

    public String getColumnType() {
        return this.columnType;
    }

    public Boolean hasLength() {
        return this.lengthFlag;
    }

    public Boolean hasPrecision() {
        return this.precisionFlag;
    }

    public Integer getDefaultLength() {
        return this.defaultLength;
    }

    public Integer getDefaultPrecision() {
        return this.defaultPrecision;
    }
}
