package cn.granitech.variantorm.metadata.fieldtype;


import cn.granitech.variantorm.metadata.FieldType;

import java.util.HashMap;
import java.util.Map;

public class FieldTypes {
    public static final String TAG_TYPE_NAME = "Tag";
    public static final FieldType OPTION = new OptionField();
    public static final String BOOLEAN_TYPE_NAME = "Boolean";
    public static final String INTEGER_TYPE_NAME = "Integer";
    public static final String EMAIL_TYPE_NAME = "Email";
    public static final String URL_TYPE_NAME = "Url";
    public static final String DATE_TYPE_NAME = "Date";
    public static final FieldType DATETIME = new DateTimeField();
    public static final String FILE_TYPE_NAME = "File";
    public static final String DECIMAL_TYPE_NAME = "Decimal";
    public static final String MULTI_OPTION_TYPE_NAME = "MultiOption";
    public static final String PERCENT_TYPE_NAME = "Percent";
    public static final String PICTURE_TYPE_NAME = "Picture";
    public static final String STATUS_TYPE_NAME = "Status";
    public static final FieldType REFERENCE = new ReferenceField();
    public static final FieldType ANYREFERENCE = new AnyReferenceField();
    public static final FieldType STATUS = new StatusField();
    public static final FieldType TAG = new TagField();
    public static final String PRIMARY_KEY_TYPE_NAME = "PrimaryKey";
    public static final FieldType INTEGER = new IntegerField();
    public static final String DATE_TIME_TYPE_NAME = "DateTime";
    public static final String OPTION_TYPE_NAME = "Option";
    public static final String REFERENCE_LIST_TYPE_NAME = "ReferenceList";
    public static final FieldType PICTURE = new PictureField();
    public static final FieldType BOOLEAN = new BooleanField();
    public static final String MONEY_TYPE_NAME = "Money";
    public static final String AREA_SELECT_TYPE_NAME = "AreaSelect";
    public static final FieldType URL = new UrlField();
    public static final FieldType DATE = new DateField();
    public static final FieldType EMAIL = new EmailField();
    public static FieldType PRIMARYKEY = new PrimaryKeyField();
    public static final String ANY_REFERENCE_TYPE_NAME = "AnyReference";
    public static final FieldType FILE = new FileField();
    public static final String REFERENCE_TYPE_NAME = "Reference";
    public static final String TEXT_AREA_TYPE_NAME = "TextArea";
    public static final FieldType PERCENT = new PercentField();
    public static final FieldType MULTIOPTION = new MultiOptionField();
    public static final String TEXT_TYPE_NAME = "Text";
    public static final FieldType TEXT = new TextField();
    public static final FieldType DECIMAL = new DecimalField();
    public static final String PASSWORD_TYPE_NAME = "Password";
    public static final FieldType TEXTAREA = new TextAreaField();
    public static final FieldType REFERENCELIST = new ReferenceListField();
    private static Map<String, FieldType> fieldTypeMap = new HashMap<>();
    public static final FieldType PASSWORD = new PasswordField();
    public static final FieldType AREASELECT = new AreaSelectField();
    public static final FieldType MONEY = new MoneyField();

    static {
        fieldTypeMap.put(PRIMARY_KEY_TYPE_NAME, PRIMARYKEY);
        fieldTypeMap.put(BOOLEAN_TYPE_NAME, BOOLEAN);
        fieldTypeMap.put(INTEGER_TYPE_NAME, INTEGER);
        fieldTypeMap.put(DECIMAL_TYPE_NAME, DECIMAL);
        fieldTypeMap.put(PERCENT_TYPE_NAME, PERCENT);
        fieldTypeMap.put(MONEY_TYPE_NAME, MONEY);
        fieldTypeMap.put(TEXT_TYPE_NAME, TEXT );
        fieldTypeMap.put(EMAIL_TYPE_NAME, EMAIL);
        fieldTypeMap.put(URL_TYPE_NAME, URL );
        fieldTypeMap.put(TEXT_AREA_TYPE_NAME, TEXTAREA );
        fieldTypeMap.put(PASSWORD_TYPE_NAME, PASSWORD );
        fieldTypeMap.put(OPTION_TYPE_NAME, OPTION  );
        fieldTypeMap.put(TAG_TYPE_NAME, TAG );
        fieldTypeMap.put(AREA_SELECT_TYPE_NAME, AREASELECT);
        fieldTypeMap.put(MULTI_OPTION_TYPE_NAME, MULTIOPTION);
        fieldTypeMap.put(STATUS_TYPE_NAME, STATUS );
        fieldTypeMap.put(PICTURE_TYPE_NAME, PICTURE);
        fieldTypeMap.put(FILE_TYPE_NAME, FILE );
        fieldTypeMap.put(DATE_TYPE_NAME, DATE );
        fieldTypeMap.put(DATE_TIME_TYPE_NAME, DATETIME );
        fieldTypeMap.put(REFERENCE_TYPE_NAME, REFERENCE );
        fieldTypeMap.put(ANY_REFERENCE_TYPE_NAME, ANYREFERENCE );
        fieldTypeMap.put(REFERENCE_LIST_TYPE_NAME, REFERENCELIST );
    }

    private  FieldTypes() {
    }



    public static FieldType getType(String typeName) {
        if (!fieldTypeMap.containsKey(typeName)) {
            throw new IllegalArgumentException("Invalid field type name: " + typeName);
        } else {
            return fieldTypeMap.get(typeName);
        }
    }
}
