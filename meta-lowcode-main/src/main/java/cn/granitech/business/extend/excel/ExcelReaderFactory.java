package cn.granitech.business.extend.excel;

import cn.granitech.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * @author ly-dourx
 */
public class ExcelReaderFactory {
    private final static Logger log = LoggerFactory.getLogger(ExcelReaderFactory.class);

    public static ExcelReader create(File excel) throws ServiceException {
        if (excel != null && excel.getName().endsWith(".xlsx")) {
            return new XExcelReader(excel);
        }
        if (excel != null && excel.getName().endsWith(".xls")) {
            return new ExcelReader(excel);
        }
        throw new ServiceException("无效 Excel 文件: " + excel);
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    protected static void close(XMLStreamReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
