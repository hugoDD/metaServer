package cn.granitech.util;

import cn.hutool.core.comparator.VersionComparator;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

public class UpgradeScriptHelper {
    private static final String VERSION_SCRIPT = "--version:";

    public static Map<String, StringBuffer> readUpgradeScript(String sqlVersion) throws IOException {
        List<String> scriptList;
        try (InputStream inputStream = (new ClassPathResource("sql/metaServer_upgrade.sql")).getInputStream()) {
            scriptList = IOUtils.readLines(inputStream, "utf-8");
        }
        Map<String, StringBuffer> scriptMap = new LinkedHashMap<>();
        StringBuffer sqlScript = new StringBuffer();
        for (String script : scriptList) {
            if (StringUtils.isBlank(script))
                continue;
            script = script.trim();
            if (script.startsWith(VERSION_SCRIPT)) {
                String version = script.substring(VERSION_SCRIPT.length()).trim();
                if (VersionComparator.INSTANCE.compare(version, sqlVersion) <= 0)
                    return scriptMap;
                sqlScript = new StringBuffer();
                scriptMap.put(version, sqlScript);
                continue;
            }
            sqlScript.append(script).append("\n");
        }
        return scriptMap;
    }

    public static StringBuffer readInitializeScript() throws IOException {
        List<String> scriptList;
        try (InputStream inputStream = (new ClassPathResource("sql/metaServer_initialize.sql")).getInputStream()) {
            scriptList = IOUtils.readLines(inputStream, "utf-8");
        }
        StringBuffer sqlScript = new StringBuffer();
        for (String script : scriptList)
            sqlScript.append(script).append("\n");
        return sqlScript;
    }
}
