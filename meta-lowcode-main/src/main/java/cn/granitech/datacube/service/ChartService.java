package cn.granitech.datacube.service;

import cn.granitech.business.query.QueryHelper;
import cn.granitech.business.query.SelectStatement;
import cn.granitech.business.service.BaseService;
import cn.granitech.business.service.RightManager;
import cn.granitech.business.service.ShareAccessService;
import cn.granitech.exception.ServiceException;
import cn.granitech.util.FilterHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.pojo.OptionModel;
import cn.granitech.web.enumration.CalcModeEnum;
import cn.granitech.web.pojo.chart.AxisFormat;
import cn.granitech.web.pojo.chart.Chart;
import cn.granitech.web.pojo.chart.ChartAxis;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class ChartService
        extends BaseService {
    @Resource
    PersistenceManager pm;
    @Resource
    RightManager rightManager;
    @Resource
    ShareAccessService shareAccessService;

    public Object queryChartData(Chart chart) {
        Entity entity = this.pm.getMetadataManager().getEntity(chart.getEntityName());
        List<ChartAxis> latitude = chart.getLatitude();
        List<ChartAxis> longitude = chart.getLongitude();

        if (CollUtil.isEmpty(latitude) && !"number".equals(chart.getChartType()) && !"pie".equals(chart.getChartType())) {

            throw new ServiceException("纬度没有选择字段！");
        }
        if (CollUtil.isEmpty(longitude)) {
            throw new ServiceException("经度没有选择字段！");
        }



        String latitudeField = "";
        StringBuilder selectFields = new StringBuilder();
        if (CollUtil.isNotEmpty(latitude)) {
            latitudeField = latitude.stream().map(ChartAxis::getFieldName).collect(Collectors.joining(","));
            selectFields.append(latitudeField).append(",");
        }

        if (CollUtil.isNotEmpty(longitude)) {


            String longitudeField = longitude.stream().map(axis -> CalcModeEnum.getCalcModeEnum(axis.getCalcMode()).getCalcSql(axis.getFieldName())).collect(Collectors.joining(","));

            selectFields.append(longitudeField);
        } else {
            throw new ServiceException("经度没有选择字段！");
        }


        String filter = FilterHelper.toFilter(chart.getFilter());
        if (!chart.isNoPrivileges()) {

            String readRightFilter = this.rightManager.getReadRightFilter(entity.getEntityCode());


            String shareRightFilter = this.shareAccessService.getShareReadRightFilter(entity.getEntityCode(), entity.getIdField());

            String rightFilter = FilterHelper.joinFilters("OR", readRightFilter, shareRightFilter);

            filter = FilterHelper.joinFilters("AND", filter, rightFilter);
        }



        Stream<ChartAxis> axisList = Stream.concat(longitude.stream(), latitude.stream());


        String orderBy = axisList.filter(axis -> StringUtils.isNotBlank(axis.getSort())).map(chartAxis -> String.format(" %s %s ", chartAxis.getFieldName(), chartAxis.getSort())).collect(Collectors.joining());



        String groupBy = latitudeField;


        StringBuilder easySql = (new StringBuilder("SELECT ")).append(selectFields).append(" FROM ").append(entity.getName());

        if (StringUtils.isNotBlank(filter)) {

            easySql.append(" WHERE ").append(filter);
        }

        if (StringUtils.isNotBlank(groupBy)) {

            easySql.append(" GROUP BY ").append(groupBy);
        }

        if (StringUtils.isNotBlank(orderBy)) {

            easySql.append(" ORDER BY ").append(orderBy);
        }


        SelectStatement selectStatement = (new QueryHelper()).compileEasySql(this.pm.getMetadataManager(), easySql.toString());

        List<List<Object>> dataList = new ArrayList<>();
        pm.getJdbcTemplate().query(selectStatement.toString(), rs -> {
            int columnCount = rs.getMetaData().getColumnCount();

            List<Object> row = new ArrayList<>();
            int index = 1;
            for (ChartAxis chartAxis : latitude) {
                if (entity.containsField(chartAxis.getFieldName()) && entity.getField(chartAxis.getFieldName()).getType() == FieldTypes.REFERENCE) {
                    row.add(new IDName(ID.valueOf(rs.getObject(index)), rs.getString(++index)));
                } else {
                    row.add(rs.getObject(index));
                }
                index++;
            }
            while (index <= columnCount) {
                row.add(rs.getObject(index));
                index++;
            }
            dataList.add(row);
        });

        if (CollUtil.isNotEmpty(latitude)) {

            for (int i = 0; i < latitude.size(); i++) {

                if (entity.containsField(latitude.get(i).getFieldName()) && entity.getField(latitude.get(i).getFieldName()).getType() == FieldTypes.OPTION) {


                    for (List<Object> data : dataList) {

                        if (data.get(i) == null) {
                            continue;
                        }

                        OptionModel option = this.pm.getQueryCache().getOption(entity.getName(), latitude.get(i).getFieldName(), (Integer)data.get(i));

                        data.set(i, option.getLabel());
                    }
                }
            }
        }



        if ("number".equals(chart.getChartType()))
             return toNumberData(longitude, dataList);

        if ("axis".equals(chart.getChartType()))
             return toLineData(latitude, longitude, dataList);

        if ("pie".equals(chart.getChartType()))
             return toPieData(latitude, longitude, dataList);

        if ("table".equals(chart.getChartType())) {

            return toTableData(latitude, longitude, dataList);
        }


        return dataList;
    }


    private Map<String, Object> toNumberData(List<ChartAxis> longitude, List<List<Object>> dataList) {

        Map<String, Object> map = new HashMap<>();

        Object data = (CollUtil.isEmpty(dataList) || CollUtil.isEmpty(dataList.get(0))) ? Integer.valueOf(0) : (dataList.get(0)).get(0);

        map.put("data", getFormatLabel(longitude.get(0).getAxisFormat(), data));

        map.put("label", longitude.get(0).getLabel());

        return map;
    }


    private Map<String, Object> toTableData(List<ChartAxis> latitude, List<ChartAxis> longitude, List<List<Object>> dataList) {

        Map<String, Object> tableMap = new HashMap<>();


        List<Map<String, String>> meta = new ArrayList<>();


        Map<String, List<String>> fieldMap = new HashMap<>();

        fieldMap.put("rows", new ArrayList<>());

        fieldMap.put("columns", new ArrayList<>());

        fieldMap.put("values", new ArrayList<>());

        for (int i = 0; i < latitude.size(); i++) {

            String field = String.valueOf(i);

            Map<String, String> metaMap = new HashMap<>();

            metaMap.put("field", field);

            metaMap.put("name", latitude.get(i).getLabel());

            meta.add(metaMap);


            fieldMap.get(latitude.get(i).isColumns() ? "columns" : "rows").add(field);
        }

        for (int i = 0; i < longitude.size(); i++) {

            String field = String.valueOf(latitude.size() + i);

            Map<String, String> metaMap = new HashMap<>();

            metaMap.put("field", field);

            metaMap.put("name", longitude.get(i).getLabel());

            meta.add(metaMap);


            fieldMap.get("values").add(field);
        }

        tableMap.put("meta", meta);

        tableMap.put("fields", fieldMap);


        List<Map<String, Object>> data = dataList.stream().map(row -> {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 0; i < row.size(); i++) {
                int longitudeIndex = i - latitude.size();
                rowMap.put(String.valueOf(i), (longitudeIndex < 0) ? row.get(i) : getFormatLabel(longitude.get(longitudeIndex).getAxisFormat(), row.get(i), false));
            }
            return rowMap;
        }).collect(Collectors.toList());

        tableMap.put("data", data);

        return tableMap;
    }


    private Map<String, Object> toLineData(List<ChartAxis> latitude, List<ChartAxis> longitude, List<List<Object>> dataList) {

        Map<String, Object> resultMap = new HashMap<>();

        List<Map<String, Object>> series = new ArrayList<>();



        if (latitude.size() == 1) {

            List<Object> xAxis = dataList.stream().map(list -> list.get(0)).collect(Collectors.toList());

            resultMap.put("xAxis", xAxis);

            List<String> yAxis = longitude.stream().map(ChartAxis::getLabel).collect(Collectors.toList());

            resultMap.put("yAxis", yAxis);


            List<List<Object>> seriesData = null;

            if (dataList.size() != 0) {

                seriesData = (List<List<Object>>) ((List) dataList.get(0)).stream().map(o -> new ArrayList()).collect(Collectors.toList());

                for (List<Object> row : dataList) {

                    for (int j = 1; j < row.size(); j++) {

                        seriesData.get(j - 1).add(getFormatLabel(longitude.get(j - 1).getAxisFormat(), row.get(j)));
                    }
                }
            }


            for (int i = 0; i < yAxis.size(); i++) {

                Map<String, Object> map = new HashMap<>();

                map.put("name", yAxis.get(i));

                map.put("data", (dataList.size() != 0) ? seriesData.get(i) : new ArrayList());

                series.add(map);
            }
        } else {

            List<Object> xAxis = dataList.stream().map(list -> list.get(0)).distinct().collect(Collectors.toList());

            resultMap.put("xAxis", xAxis);


            List<Object> yAxis = dataList.stream().map(list -> list.get(1)).distinct().collect(Collectors.toList());

            resultMap.put("yAxis", yAxis);




            Map<Object, Map<Object, Object>> dataMap = dataList.stream().collect(Collectors.groupingBy(data -> data.get(0),
                                 Collectors.toMap(data -> data.get(1), data -> data.get(2))));


            for (Object y : yAxis) {

                Map<String, Object> seriesMap = new HashMap<>();

                seriesMap.put("name", y);



                List<Object> data = xAxis.stream().map(x -> getFormatLabel(longitude.get(0).getAxisFormat(), (ObjectUtil.isNull(dataMap.get(x)) || ObjectUtil.isNull(dataMap.get(x).get(y))) ? Integer.valueOf(0) : dataMap.get(x).get(y))).collect(Collectors.toList());

                seriesMap.put("data", data);


                series.add(seriesMap);
            }
        }

        resultMap.put("series", series);

        return resultMap;
    }


    private List<Map<Object, Object>> toPieData(List<ChartAxis> latitude, List<ChartAxis> longitude, List<List<Object>> dataList) {

        if (latitude.size() == 0) {

            List<Map<Object, Object>> list = new ArrayList<>();

            List<Object> row = dataList.get(0);

            for (int i = 0; i < row.size(); i++) {

                Map<Object, Object> dataMap = new HashMap<>();

                dataMap.put("name", longitude.get(i).getLabel());

                dataMap.put("value", getFormatLabel(longitude.get(i).getAxisFormat(), row.get(i)));

                list.add(dataMap);
            }

            return list;
        }













        List<Map<Object, Object>> result = dataList.stream().map(row -> {
            Map<Object, Object> dataMap = new HashMap<>();
            dataMap.put("name", row.get(0));
            dataMap.put("value", getFormatLabel(longitude.get(0).getAxisFormat(), row.get(1)));
            Map<String, Object> other = new HashMap<>();
            for (int i = 1; i < row.size(); i++) {
                ChartAxis chartAxis = longitude.get(i - 1);
                other.put(chartAxis.getLabel(), getFormatLabel(chartAxis.getAxisFormat(), row.get(i)));
            }
            dataMap.put("other", other);
            return dataMap;
        }).collect(Collectors.toList());

        return result;
    }


    private Object getFormatLabel(AxisFormat axisFormat, Object num) {

        return getFormatLabel(axisFormat, num, true);
    }


    private Object getFormatLabel(AxisFormat axisFormat, Object num, boolean intResult) {

        if (ObjectUtil.isNull(num)) {

            num = Integer.valueOf(0);
        }

        if (ObjectUtil.isNull(axisFormat)) {

            return num;
        }

        StringBuffer format = new StringBuffer("0");

        if (!intResult && axisFormat.isThousandsSeparator()) {

            format = new StringBuffer("#,##0");
        }

        if (axisFormat.getDecimalPlaces() > 0) {

            format.append(".").append(String.join("", Collections.nCopies(axisFormat.getDecimalPlaces(), "0")));
        }

        String formatValue = (new DecimalFormat(format.toString())).format(num);

        if (!intResult && StringUtils.isNotBlank(axisFormat.getNumericUnits())) {

            formatValue = formatValue + axisFormat.getNumericUnits();
        }

        return formatValue;
    }


    public void updateDefault(ID chatId, boolean isDefault) {

        Entity entity = this.pm.getMetadataManager().getEntity("Chart");

        String idName = entity.getIdField().getName();

        if (isDefault) {

            List<EntityRecord> entityRecords = queryListRecord("Chart", "defaultChart = 1", null, null, null, idName);

            for (EntityRecord entityRecord : entityRecords) {

                updateDefault(entityRecord.id(), false, idName);
            }

            updateDefault(chatId, true, idName);
        } else {

            updateDefault(chatId, false, idName);
        }
    }


    private void updateDefault(ID chatId, boolean isDefault, String idName) {

        EntityRecord chat = this.pm.newRecord("Chart");

        chat.setFieldValue(idName, chatId);

        chat.setFieldValue("defaultChart", Boolean.valueOf(isDefault));

        updateRecord(chat);
    }
}



