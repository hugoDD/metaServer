package cn.granitech.business.extend.excel;

import cn.granitech.exception.ServiceException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XExcelReader extends ExcelReader {
    private final static Logger log = LoggerFactory.getLogger(XExcelReader.class);
    private final OPCPackage pkg;
    private final ReadOnlySharedStringsTable sharedStringsTable;
    private final XSSFReader xssfReader;
    private XMLStreamReader cellReader;
    private int sheetIndex = 0;

    public XExcelReader(File excel) {
        try {
            this.pkg = OPCPackage.open(excel, PackageAccess.READ);
            this.xssfReader = new XSSFReader(this.pkg);
            this.sharedStringsTable = new ReadOnlySharedStringsTable(this.pkg);
        } catch (Exception e) {
            close();
            throw new ServiceException(e);
        }
    }

    @Override
    public String[] getSheetNames() {
        List<String> names = new ArrayList<>();
        try {
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) this.xssfReader.getSheetsData();
            while (iter.hasNext()) {
                InputStream is = iter.next();
                names.add(iter.getSheetName());
                ExcelReaderFactory.close(is);
            }
            return names.toArray(new String[0]);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void sheetAt(int index) {
        if (this.cellReader != null) {
            ExcelReaderFactory.close(this.cellReader);
            this.cellReader = null;
        }
        try {
            XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) this.xssfReader.getSheetsData();
            int currentIndex = 0;
            while (true) {
                int currentIndex2 = currentIndex;
                if (!iter.hasNext()) {
                    break;
                }
                InputStream is = iter.next();
                currentIndex = currentIndex2 + 1;
                if (currentIndex2 == index) {
                    this.cellReader = XMLInputFactory.newInstance().createXMLStreamReader(is);
                    while (this.cellReader.hasNext()) {
                        this.cellReader.next();
                        if (this.cellReader.isStartElement() && "sheetData".equals(this.cellReader.getLocalName())) {
                            break;
                        }
                    }
                } else {
                    ExcelReaderFactory.close(is);
                }
            }
            if (this.cellReader == null) {
                throw new ServiceException("无效 SHEET 位置: " + index);
            }
            this.sheetIndex = index;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getRowCount() {
        sheetAt(this.sheetIndex);
        int rowCount = 0;
        try {
            while (this.cellReader.hasNext()) {
                this.cellReader.next();
                if (this.cellReader.isStartElement() && "row".equals(this.cellReader.getLocalName())) {
                    rowCount++;
                }

            }
        } catch (XMLStreamException e) {
            throw new ServiceException(e);
        }
        sheetAt(this.sheetIndex);
        return rowCount;
    }

    @Override
    public boolean hasNext() {
        if (this.cellReader == null) {
            sheetAt(this.sheetIndex);
        }
        try {
            return this.cellReader.hasNext();
        } catch (XMLStreamException e) {
            return false;
        }
    }

    @Override
    public Cell[] next() {
        if (this.cellReader == null) {
            sheetAt(this.sheetIndex);
        }
        try {
            do {
                this.cellReader.next();
                if (this.cellReader.isStartElement() && "row".equals(this.cellReader.getLocalName())) {
                    return readRow();
                }
            } while (this.cellReader.hasNext());
        } catch (XMLStreamException e) {
            throw new ServiceException(e);
        }
        return null;
    }

    private Cell[] readRow() throws XMLStreamException {
        List<Cell> cellList = new ArrayList<>();
        while (this.cellReader.hasNext()) {
            this.cellReader.next();
            if (!this.cellReader.isStartElement()) {
                if (this.cellReader.isEndElement() && "row".equals(this.cellReader.getLocalName())) {
                    break;
                }
            } else if ("c".equals(this.cellReader.getLocalName())) {
                CellReference cellReference = new CellReference(this.cellReader.getAttributeValue(null, "r"));
                while (cellList.size() < cellReference.getCol()) {
                    cellList.add(Cell.NULL);
                }
                cellList.add(trimToStringCell(readCell(this.cellReader.getAttributeValue(null, "t"))));
            }
        }
        return cellList.toArray(new Cell[0]);
    }

    private String readCell(String cellType) throws NumberFormatException, XMLStreamException {
        while (this.cellReader.hasNext()) {
            this.cellReader.next();
            if (!this.cellReader.isStartElement()) {
                if (this.cellReader.isEndElement() && "c".equals(this.cellReader.getLocalName())) {
                    break;
                }
            } else if ("v".equals(this.cellReader.getLocalName())) {
                if (!"s".equals(cellType)) {
                    return this.cellReader.getElementText();
                }
                return new XSSFRichTextString(this.sharedStringsTable.getEntryAt(Integer.parseInt(this.cellReader.getElementText()))).toString();
            }
        }
        return "";
    }

    @Override
    public void close() {
        super.close();
        ExcelReaderFactory.close(this.cellReader);
        ExcelReaderFactory.close(this.pkg);
    }
}
