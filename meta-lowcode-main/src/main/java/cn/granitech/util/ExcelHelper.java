package cn.granitech.util;

import cn.granitech.business.extend.excel.Cell;
import cn.granitech.exception.ServiceException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelHelper {
    public static final int MAX_UNLIMIT = -1;

    public static List<Cell[]> readExcel(File excel) {
        return readExcel(excel, MAX_UNLIMIT, true);
    }

    public static List<Cell[]> readExcel(File excel, final int maxRows, final boolean hasHead) {
        final List<Cell[]> rows = new ArrayList<>();
        final AtomicInteger rowNo = new AtomicInteger(0);
        try(InputStream is = Files.newInputStream(excel.toPath());
            BufferedInputStream bis = new BufferedInputStream(is)) {
            EasyExcel.read(bis, null, new AnalysisEventListener() {
                public void invokeHeadMap(Map headMap, AnalysisContext context) {
                    if (hasHead) {
                        invoke(headMap, context);
                    } else {
                        rowNo.incrementAndGet();
                    }
                }

                public void invoke(Object data, AnalysisContext analysisContext) {
                    if (maxRows > 0 && rows.size() >= maxRows)
                        return;
                    Map<Integer, String> dataMap = (Map<Integer, String>)data;
                    List<Cell> row = new ArrayList<>();
                    for (int i = 0; i < dataMap.size(); i++)
                        row.add(new Cell(dataMap.get(i), rowNo.get(), i));
                    rows.add(row.toArray(new Cell[0]));
                    rowNo.incrementAndGet();
                }

                public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
            }).sheet().doRead();
        } catch (IOException e) {
            throw new ServiceException(e);
        }
        return rows;
    }
}
