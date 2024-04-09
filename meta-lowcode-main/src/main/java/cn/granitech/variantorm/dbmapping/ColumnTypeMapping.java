package cn.granitech.variantorm.dbmapping;

import cn.granitech.variantorm.pojo.FieldColumnType;

import java.util.HashMap;
import java.util.Map;

public class ColumnTypeMapping {

    private static final Map<String, FieldColumnType> COLUMN_TYPE = new HashMap<>();

    public static FieldColumnType getColumnType(String fieldType) {
        return COLUMN_TYPE.get(fieldType);
    }

    static {
        COLUMN_TYPE.put("PrimaryKey",new FieldColumnType("CHAR(40)"));
        COLUMN_TYPE.put("Boolean",new FieldColumnType("TINYINT"));
        COLUMN_TYPE.put("Integer",new FieldColumnType("INT(11)"));
        COLUMN_TYPE.put("Decimal",new FieldColumnType("DECIMAL(18,%d)"));
        COLUMN_TYPE.put("Percent",new FieldColumnType("SMALLINT"));
        COLUMN_TYPE.put("Money",new FieldColumnType("DECIMAL(18,%d)"));
        COLUMN_TYPE.put("Text",new FieldColumnType("VARCHAR(%d)"));
        COLUMN_TYPE.put("Email",new FieldColumnType("VARCHAR(%d)"));
        COLUMN_TYPE.put("Url",new FieldColumnType("VARCHAR(%d)"));
        COLUMN_TYPE.put("TextArea",new FieldColumnType("TEXT"));
        COLUMN_TYPE.put("Password",new FieldColumnType("VARCHAR(200)"));
        COLUMN_TYPE.put("Option",new FieldColumnType("SMALLINT"));
        COLUMN_TYPE.put("Status",new FieldColumnType("SMALLINT"));
        COLUMN_TYPE.put("Tag",new FieldColumnType("VARCHAR(300)"));
        COLUMN_TYPE.put("AreaSelect",new FieldColumnType("VARCHAR(300)"));
        COLUMN_TYPE.put("Date",new FieldColumnType("DATE"));
        COLUMN_TYPE.put("DateTime",new FieldColumnType("DATETIME"));
        COLUMN_TYPE.put("Picture",new FieldColumnType("TEXT"));
        COLUMN_TYPE.put("File",new FieldColumnType("TEXT"));
        COLUMN_TYPE.put("Reference",new FieldColumnType("CHAR(40)"));
        COLUMN_TYPE.put("AnyReference",new FieldColumnType("CHAR(40)"));
    }


}
