package cn.granitech.report.utils;

import cn.granitech.report.pojo.Field;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Report {
    private JSONArray workBook;
    private List<String> fieldNameList;
    private Map<String, List<String>> subEntityFieldMap;
    private Map<String, Field> formatFieldMap;

    public Report(String reportJson) {
        JSONArray workBook = JSONArray.parseArray(reportJson, new com.alibaba.fastjson2.JSONReader.Feature[0]);
        this.workBook = workBook;
        this.fieldNameList = new ArrayList<>();
        this.subEntityFieldMap = new HashMap<>();
        this.formatFieldMap = new HashMap<>();

        ReportUtils.workBookFormat(workBook, this.fieldNameList, this.subEntityFieldMap, this.formatFieldMap);
    }


    public void exportEntityData(String entityKey, Map<String, Object> resultMap, OutputStream outputStream) {
        JSONObject resultData = JSONObject.parseObject(JSON.toJSONString(resultMap));

        ReportUtils.dataFormat(entityKey, this.formatFieldMap, resultData);
        InputStream inputStream = ReportExcelUtil.exportExcel(this.workBook);
        ReportUtils.fillByJson(entityKey, this.workBook, resultData, inputStream, outputStream);
    }

    public JSONArray getWorkBook() {
        return this.workBook;
    }

    public void setWorkBook(JSONArray workBook) {
        this.workBook = workBook;
    }

    public List<String> getFieldNameList() {
        return this.fieldNameList;
    }

    public void setFieldNameList(List<String> fieldNameList) {
        this.fieldNameList = fieldNameList;
    }

    public Map<String, List<String>> getSubEntityFieldMap() {
        return this.subEntityFieldMap;
    }

    public void setSubEntityFieldMap(Map<String, List<String>> subEntityFieldMap) {
        this.subEntityFieldMap = subEntityFieldMap;
    }

    public Map<String, Field> getFormatFieldMap() {
        return this.formatFieldMap;
    }

    public void setFormatFieldMap(Map<String, Field> formatFieldMap) {
        this.formatFieldMap = formatFieldMap;
    }
}



