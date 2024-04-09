package cn.granitech.business.service.dataimport;

import cn.granitech.business.extend.excel.Cell;
import cn.granitech.util.ExcelHelper;

import java.io.File;
import java.util.List;

public class DataFileParser {
    public static final String UTF8 = "utf-8";
    private final File sourceFile;

    public DataFileParser(File sourceFile2) {
        this.sourceFile = sourceFile2;
    }

    public File getSourceFile() {
        return this.sourceFile;
    }

    public int getRowsCount() {
        return parse().size();
    }

    public List<Cell[]> parse() {
        return parse(Integer.MAX_VALUE);
    }

    public List<Cell[]> parse(int maxRows) {
        return ExcelHelper.readExcel(this.sourceFile, maxRows, true);
    }
}
