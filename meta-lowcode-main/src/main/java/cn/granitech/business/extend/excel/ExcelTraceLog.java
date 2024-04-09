package cn.granitech.business.extend.excel;

import cn.granitech.variantorm.metadata.ID;

public class ExcelTraceLog {
    public static final int CREATED = 2;
    public static final int ERROR = 4;
    public static final int SKIP = 1;
    public static final int UPDATED = 3;
    private String message;
    private ID recordId;
    private int rowNo;
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public int getRowNo() {
        return this.rowNo;
    }

    public void setRowNo(int rowNo2) {
        this.rowNo = rowNo2;
    }

    public ID getRecordId() {
        return this.recordId;
    }

    public void setRecordId(ID recordId2) {
        this.recordId = recordId2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }
}
