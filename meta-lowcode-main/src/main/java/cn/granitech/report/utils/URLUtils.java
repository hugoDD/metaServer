package cn.granitech.report.utils;

import cn.hutool.core.util.URLUtil;

import java.util.HashMap;
import java.util.Map;


public class URLUtils {
    private static final Map<String, String> urlCharacter = new HashMap<>();

    static {
        urlCharacter.put("%3A", ":");
        urlCharacter.put("%5C", "\\");
        urlCharacter.put("%2F", "/");
        urlCharacter.put("%3F", "?");
        urlCharacter.put("%26", "&");
    }


    public static String urlEncode(String url) {
        String encodeUrl = URLUtil.encode(url);
        for (String key : urlCharacter.keySet()) {
            encodeUrl = encodeUrl.replace(key, urlCharacter.get(key));
        }
        return encodeUrl;
    }
}



