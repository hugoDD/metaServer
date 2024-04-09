package cn.granitech.variantorm.util;


import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StringHelper {

    public static String join(Object[] objects, String separator) {
        return join(Arrays.asList(objects).iterator(), separator);
    }

    public static boolean isEmpty(String string) {
        return string == null || string.trim().equals("");
    }

    public static List<String> extractString(String inString, String startTag, String endTag) {
        List<String> list = new LinkedList<>();
        int startTagIndex = inString.indexOf(startTag);
        int endTagIndex = inString.indexOf(endTag);
        if (startTagIndex != -1 && endTagIndex != -1) {
            String str2 = inString.substring(startTagIndex + startTag.length(), endTagIndex);
            list.add(str2);
        }
        return list;
    }

    public static String join(Iterator<Object> objects, String separator) {
        StringBuilder joinStr = new StringBuilder();
        if (objects.hasNext()) {
            joinStr.append(objects.next());
        }

        while(objects.hasNext()) {
            joinStr.append(separator).append(objects.next());
        }

        return joinStr.toString();
    }

    public static String repeat(String string, int times) {
        StringBuilder repeatStr = new StringBuilder(string.length() * times);

        for(int var10000 = 0; var10000 < times; var10000 ++) {
            repeatStr.append(string);
        }

        return repeatStr.toString();
    }
}

