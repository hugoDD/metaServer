package cn.granitech.business.service.dataimport;

import cn.granitech.business.extend.excel.Cell;
import cn.granitech.business.extend.excel.ExcelTraceLog;
import cn.granitech.business.service.CrudService;
import cn.granitech.business.task.HeavyTask;
import cn.granitech.util.MetadataHelper;
import cn.granitech.util.SpringHelper;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataImporter extends HeavyTask<Integer> {
    private final ImportRule rule;
    private final List<ExcelTraceLog> traceLogs = new ArrayList<>();
    private String cellTraces = null;

    public DataImporter(ImportRule rule2) {
        this.rule = rule2;
    }

    public Integer execute() {
        boolean isNew;
        List<Cell[]> rows = new DataFileParser(this.rule.getSourceFile()).parse();
        setTotal(rows.size() - 1);
        CrudService crudService = SpringHelper.getBean(CrudService.class);
        Iterator<Cell[]> it = rows.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Cell[] row = it.next();
            if (isInterrupt()) {
                setInterrupted();
                break;
            }
            Cell fc = (row == null || row.length == 0) ? null : row[0];
            if (!(fc == null || fc.getRowNo() == 0)) {
                ExcelTraceLog excelTraceLog = new ExcelTraceLog();
                try {
                    EntityRecord record = checkoutRecord(row, crudService.newRecord(this.rule.getToEntity().getName()));
                    excelTraceLog.setRowNo(fc.getRowNo());
                    if (record == null) {
                        excelTraceLog.setType(1);
                    } else {
                        isNew = record.id() == null;
                        crudService.saveOrUpdateRecord(record.id(), record);
                        addSucceeded();
                        excelTraceLog.setType(isNew ? 2 : 3);
                        excelTraceLog.setRecordId(record.id());
                        excelTraceLog.setMessage(this.cellTraces);
                    }
                } catch (Exception ex) {
                    excelTraceLog.setType(4);
                    excelTraceLog.setMessage(ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    this.traceLogs.add(excelTraceLog);
                }
                addCompleted();
            }
        }
        return Integer.valueOf(getSucceeded());
    }

    public void completedAfter() {
        super.setCompletedTime(DateUtil.date());
        this.rule.getSourceFile().delete();
    }

    /* access modifiers changed from: protected */
    public EntityRecord checkoutRecord(Cell[] row, EntityRecord recordHub) {
        RecordCheckout recordCheckout = new RecordCheckout(this.rule.getFieldsMapping());
        EntityRecord checkout = recordCheckout.checkout(recordHub, row);
        if (recordCheckout.getTraceLogs().isEmpty()) {
            this.cellTraces = null;
        } else {
            this.cellTraces = StringUtils.join(recordCheckout.getTraceLogs(), ", ");
        }
        if (this.rule.getRepeatOpt() >= 3) {
            return checkout;
        }
        EntityRecord repeat = findRepeatedRecordId(this.rule.getRepeatFields(), recordHub);
        if (repeat != null && this.rule.getRepeatOpt() == 2) {
            return null;
        }
        if (repeat == null || this.rule.getRepeatOpt() != 1) {
            return checkout;
        }
        checkout.setFieldValue(recordHub.getEntity().getIdField().getName(), repeat.id());
        for (String fieldName : recordHub.getValuesMap().keySet()) {
            if (!MetadataHelper.isIgnoreFieldByName(fieldName)) {
                checkout.setFieldValue(fieldName, recordHub.getValuesMap().get(fieldName));
            }
        }
        return checkout;
    }

    /* access modifiers changed from: protected */
    public EntityRecord findRepeatedRecordId(Field[] repeatFields, EntityRecord record) {
        Entity entity = record.getEntity();
        CrudService crudService = SpringHelper.getBean(CrudService.class);
        StringBuilder filter = new StringBuilder("1=1");
        for (Field repeatField : repeatFields) {
            String fieldName = repeatField.getName();
            filter.append(" AND ").append(String.format(" [%s] = '%s' ", fieldName, record.getFieldValue(fieldName)));
        }
        return crudService.queryOneRecord(entity.getName(), filter.toString(), null, null);
    }

    public List<ExcelTraceLog> getTraceLogs() {
        return this.traceLogs;
    }
}
