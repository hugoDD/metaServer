package cn.granitech.variantorm.util;


public class BooleanHelper {
    public static boolean parseBoolean(String value) {
        if (!isValidBooleanString(value)) {
            System.out.println((new StringBuilder()).insert(0, "Invalid boolean value is: ").append(value));
            throw new IllegalArgumentException(value);
        }
        return (value.trim().equals("1") || value.trim().equalsIgnoreCase("true"));
    }

    public static boolean isValidBooleanString(String value) {
        if (value == null) {
            return false;
        }
        String a=value.trim().toLowerCase();
        return a.equals("1") || a.equals("0") || a.equals("true") || a.equals("false");
    }
}
