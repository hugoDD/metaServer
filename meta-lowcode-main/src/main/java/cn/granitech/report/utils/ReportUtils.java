
package cn.granitech.report.utils;

import cn.granitech.report.enumration.FormatTypeEnum;
import cn.granitech.report.pojo.Field;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportUtils {
    private static final String CELL_LIST = "celldata";
    private static final String CELL_DATA = "v";
    private static final String CELL_DATA_MONITOR = "m";
    private static final String CELL_DATA_PS = "ps";
    private static final String CELL_DATA_PS_VALUE = "value";
    private static final String FORMAT_TEXT = "{%s}";
    private static final String OBJECT_SEPARATOR = ".";

    public ReportUtils() {
    }

    public static void workBookFormat(JSONArray workBook, List<String> fieldNameList, Map<String, List<String>> subEntityFieldMap, Map<String, Field> formatFieldMap) {
        workBook.forEach((workSheet) -> {
            JSONObject sheet = (JSONObject)workSheet;
            JSONArray cellList = sheet.getJSONArray(CELL_LIST);
            cellList.forEach((cell) -> cellFormat((JSONObject)cell, fieldNameList, subEntityFieldMap, formatFieldMap));
        });
    }

    private static void cellFormat(JSONObject cell, List<String> fieldNameList, Map<String, List<String>> subEntityFieldMap, Map<String, Field> formatFieldMap) {
        if (null != cell) {
            JSONObject cellData = cell.getJSONObject(CELL_DATA);
            JSONObject ps = cellData.getJSONObject(CELL_DATA_PS);
            if (null != ps) {
                Field Field = JSON.parseObject(ps.getString(CELL_DATA_PS_VALUE), Field.class);
                String cellValue;
                if (Field.getCode().contains(OBJECT_SEPARATOR)) {
                    String[] array = Field.getCode().split("\\.");
                    cellValue = array[0];
                    String attributeName = array[1];
                    List<String> attributeList = subEntityFieldMap.get(cellValue);
                    if (null == attributeList) {
                        attributeList = new ArrayList<>();
                    }

                    attributeList.add(attributeName);
                    subEntityFieldMap.put(cellValue, attributeList);
                } else {
                    fieldNameList.add(Field.getCode());
                }

                String code = Field.getCode();
                if (null != Field.getFormatMap() && Field.getFormatMap().size() > 0) {
                    code = code + "_" + UUID.randomUUID();
                    formatFieldMap.put(code, Field);
                }

                cellValue = String.format(FORMAT_TEXT, code);
                cellData.fluentPut(CELL_DATA, cellValue).put(CELL_DATA_MONITOR, cellValue);
            }
        }
    }

    public static void dataFormat(String entityKey, Map<String, Field> formatMap, JSONObject data) {
        formatMap.keySet().forEach((key) -> {
            Field Field = formatMap.get(key);
            String code = Field.getCode();
            Map<String, Object> map = Field.getFormatMap();
            if (!code.contains(OBJECT_SEPARATOR)) {
                JSONObject mainObj = data.getJSONObject(entityKey);
                String formatValue = mainObj.getString(code);

                for (String s : map.keySet()) {
                    FormatTypeEnum formatEnum = FormatTypeEnum.getFormatEnumByKey(s);
                    if (formatEnum != null) {
                        formatValue = formatEnum.dataFormat(formatValue, map.get(s));
                    }
                }

                mainObj.put(key, formatValue);
            } else {
                String[] array = Field.getCode().split("\\.");
                String formatValueFirst = array[0];
                String attributeName = array[1];
                String formatKey = key.substring(key.indexOf(OBJECT_SEPARATOR) + 1);
                JSONArray jsonArray = data.getJSONArray(formatValueFirst);
                jsonArray.forEach((object) -> {
                    JSONObject entity = (JSONObject)object;
                    String formatValue = entity.getString(attributeName);

                    for (String formatKeyx : map.keySet()) {
                        FormatTypeEnum formatEnum = FormatTypeEnum.getFormatEnumByKey(formatKeyx);
                        if (formatEnum != null) {
                            formatValue = formatEnum.dataFormat(formatValue, map.get(formatKeyx));
                        }
                    }

                    entity.put(formatKey, formatValue);
                });
            }

        });
    }

    public static void fillByJson(String entityKey, JSONArray workBookJson, JSONObject jsonObj, InputStream template, OutputStream outputStream) {
        ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(template).build();

        Map<String, Object> mainMap = (Map<String,Object>)jsonObj.get(entityKey);
        dataCleaning(mainMap);
        jsonObj.remove(entityKey);
        dataArrayCleaning(jsonObj);
        workBookJson.forEach((workSheet) -> {
            JSONObject sheet = (JSONObject)workSheet;
            String sheetName = sheet.getString("name");
            WriteSheet writeSheet = EasyExcel.writerSheet(sheetName).build();
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            jsonObj.keySet().forEach((key) -> excelWriter.fill(new FillWrapper(key, (List)jsonObj.get(key)), fillConfig, writeSheet));
            excelWriter.fill(mainMap, writeSheet);
        });

    }

    private static void dataArrayCleaning(JSONObject jsonObj) {
        jsonObj.keySet().forEach((key) -> {
            List<Map<String, Object>> list = (List)jsonObj.get(key);

            for (Map<String, Object> stringObjectMap : list) {
                dataCleaning(stringObjectMap);
            }

        });
    }

    private static void dataCleaning(Map<String, Object> map) {
        if (map != null) {

            for (String key : map.keySet()) {
                if (map.get(key) != null) {
                    if (map.get(key) instanceof ArrayList) {
                        List<String> list = (ArrayList<String>) map.get(key);
                        map.put(key, ReportExcelUtil.writeByUrl(list));
                    } else if (map.get(key) != null) {
                        map.put(key, map.get(key).toString());
                    }
                }
            }

        }
    }
}
