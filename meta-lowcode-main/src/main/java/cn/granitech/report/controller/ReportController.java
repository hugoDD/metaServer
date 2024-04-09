package cn.granitech.report.controller;

import cn.granitech.business.service.CrudService;
import cn.granitech.report.service.ReportService;
import cn.granitech.report.utils.Report;
import cn.granitech.util.ResponseHelper;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.PersistenceManager;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.web.pojo.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/plugins/mannerReport")
public class ReportController {
    @Resource
    ReportService reportService;
    @Resource
    CrudService crudService;
    @Resource
    PersistenceManager pm;

    @GetMapping({"getEntityList"})
    public ResponseBean<List<Map<String, Object>>> getEntityList(@RequestParam(name = "reportConfigId") String reportConfigId) {
        EntityRecord reportRecord = this.crudService.queryById(new ID(reportConfigId), "entityCode");
        Entity entity = this.pm.getMetadataManager().getEntity((Integer) reportRecord.getFieldValue("entityCode"));

        List<Map<String, Object>> resultList = new ArrayList<>();
        resultList.addAll(this.reportService.getFieldListByEntity(entity));
        resultList.addAll(this.reportService.getFieldListByReference(entity));

        return ResponseHelper.ok(resultList);
    }


    @GetMapping({"getReportConfigList"})
    public ResponseBean<?> getReportConfigList(@RequestParam("entityCode") int entityCode) {
        List<EntityRecord> entityRecords = this.reportService.queryListRecord("ReportConfig",
                String.format("entityCode = %s AND isDisabled = 0", entityCode), null, null, null, "reportConfigId", "reportName");

        List<Map<String, Object>> resultList = entityRecords.stream().map(EntityRecord::getValuesMap).collect(Collectors.toList());
        return ResponseHelper.ok(resultList, "success");
    }


    @GetMapping({"exportExcelTemplate"})
    public void exportEntityData(HttpServletResponse response, @RequestParam(name = "reportConfigId") String reportConfigId, @RequestParam(name = "entityId") String entityId) throws Exception {
        Entity entity = this.pm.getMetadataManager().getEntity((new ID(entityId)).getEntityCode());

        EntityRecord reportRecord = this.crudService.queryById(new ID(reportConfigId), "reportName", "reportJson");

        Report report = new Report(reportRecord.getFieldValue("reportJson"));
        Map<String, Object> dataMap = this.reportService.getReportData(report, reportConfigId, entityId);
        String reportName = reportRecord.getFieldValue("reportName");

        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(reportName, "utf-8") + ".xlsx");
        response.addHeader("filename", URLEncoder.encode(reportName, "utf-8") + ".xlsx");
        report.exportEntityData(entity.getName(), dataMap, response.getOutputStream());
    }
}



