package cn.granitech.datacube.controller;

import cn.granitech.datacube.service.ChartService;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.web.pojo.ResponseBean;
import cn.granitech.web.pojo.chart.Chart;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping({"/plugins/metaDataCube/chart"})
public class ChartController {
    @Resource
    ChartService chartService;

    @PostMapping({"queryChartData"})
    public ResponseBean<Object> queryChartData(@RequestBody Chart chart) {
        Object result = this.chartService.queryChartData(chart);
        return ResponseHelper.ok(result);
    }


    @PostMapping({"updateDefault"})
    public ResponseBean updateDefault(@RequestParam("id") String chartId, @RequestParam("defaultChart") boolean defaultChart) {
        this.chartService.updateDefault(ID.valueOf(chartId), defaultChart);
        return ResponseHelper.ok();
    }
}



