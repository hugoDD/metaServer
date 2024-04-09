package cn.granitech.business.extend.excel;

import cn.granitech.exception.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ly-dourx
 */
public class ExcelReader implements Iterator<Cell[]>, Closeable {
    protected int rowCount = 0;
    protected int rowIndex = 0;
    private Workbook workbook;
    private Sheet sheet;

    protected ExcelReader() {
    }

    public ExcelReader(File excel) {
        try {
            this.workbook = new HSSFWorkbook(POIFSFileSystem.create(excel));
        } catch (IOException var3) {
            this.close();
            throw new ServiceException(var3);
        }

        this.sheetAt(0);
    }

    public String[] getSheetNames() {
        List<String> names = new ArrayList<>();

        for (int i = 0; i < this.workbook.getNumberOfSheets(); ++i) {
            names.add(this.workbook.getSheetName(i));
        }

        return names.toArray(new String[0]);
    }

    public void sheetAt(int index) {
        if (index > this.workbook.getNumberOfSheets()) {
            throw new ServiceException("无效 SHEET 位置: " + index);
        } else {
            this.sheet = this.workbook.getSheetAt(index);
            this.rowCount = this.sheet.getPhysicalNumberOfRows();
            this.rowIndex = 0;
        }
    }

    public int getRowCount() {
        return this.rowCount;
    }

    public int getRowIndex() {
        return this.rowIndex;
    }

    @Override
    public boolean hasNext() {
        return this.getRowIndex() < this.getRowCount();
    }

    @Override
    public Cell[] next() {
        List<Cell> rowValues = new ArrayList<>();
        Row row = this.sheet.getRow(this.rowIndex++);

        for (int i = 0; i < row.getPhysicalNumberOfCells(); ++i) {
            org.apache.poi.ss.usermodel.Cell cell = row.getCell(i);
            rowValues.add(this.readCell(cell));
        }

        return rowValues.toArray(new Cell[0]);
    }

    private Cell readCell(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell == null) {
            return Cell.NULL;
        } else {
            CellType type = cell.getCellTypeEnum();
            if (type == CellType.BOOLEAN) {
                return new Cell(cell.getBooleanCellValue());
            } else if (type != CellType.NUMERIC && type != CellType.FORMULA) {
                return type == CellType.STRING ? this.trimToStringCell(cell.getStringCellValue()) : Cell.NULL;
            } else {
                return DateUtil.isCellDateFormatted(cell) ? new Cell(cell.getDateCellValue()) : new Cell(cell.getNumericCellValue());
            }
        }
    }

    protected Cell trimToStringCell(String cellText) {
        if (cellText == null) {
            return Cell.NULL;
        } else {
            cellText = cellText.trim();
            return new Cell(cellText);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        ExcelReaderFactory.close(this.workbook);
    }
}
