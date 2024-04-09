package cn.granitech.business.service;

import cn.granitech.web.pojo.TableHeaderColumn;

import java.util.HashMap;
import java.util.Map;

public class TableHeaderMapping {
    private static final Map<String, TableHeaderColumn> HEADER_TYPE_MAP = new HashMap();

    static {
        HEADER_TYPE_MAP.put("Boolean", new TableHeaderColumn("100", "center"));
        HEADER_TYPE_MAP.put("Integer", new TableHeaderColumn("120", "right"));
        HEADER_TYPE_MAP.put("Decimal", new TableHeaderColumn("120", "right"));
        HEADER_TYPE_MAP.put("Percent", new TableHeaderColumn("120", "right"));
        HEADER_TYPE_MAP.put("Money", new TableHeaderColumn("120", "right"));
        HEADER_TYPE_MAP.put("Text", new TableHeaderColumn("120", "left"));
        HEADER_TYPE_MAP.put("TextArea", new TableHeaderColumn("200", "left"));
        HEADER_TYPE_MAP.put("Option", new TableHeaderColumn("120", "left"));
        HEADER_TYPE_MAP.put("Tag", new TableHeaderColumn("160", "left"));
        HEADER_TYPE_MAP.put("Date", new TableHeaderColumn("100", "left"));
        HEADER_TYPE_MAP.put("DateTime", new TableHeaderColumn("160", "left"));
        HEADER_TYPE_MAP.put("Reference", new TableHeaderColumn("120", "left"));
    }

    public static TableHeaderColumn getHeaderColumn(String fieldType) {
        return HEADER_TYPE_MAP.get(fieldType);
    }

    public static void setHeaderColumnDefaultProps(String fieldType, TableHeaderColumn thc) {
        if (HEADER_TYPE_MAP.containsKey(fieldType)) {
            TableHeaderColumn defaultThc = HEADER_TYPE_MAP.get(fieldType);
            thc.setWidth(defaultThc.getWidth());
            thc.setAlign(defaultThc.getAlign());
        }
    }
}
