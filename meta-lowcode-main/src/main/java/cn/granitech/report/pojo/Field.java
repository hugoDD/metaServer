package cn.granitech.report.pojo;

import java.util.Map;


public class Field {
    private String name;
    private String type;
    private String code;
    private Map<String, Object> formatMap;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, Object> getFormatMap() {
        return this.formatMap;
    }

    public void setFormatMap(Map<String, Object> formatMap) {
        this.formatMap = formatMap;
    }
}



