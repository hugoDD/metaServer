package cn.granitech.variantorm.exception;

public class DataAccessException extends MetadataSpacesException {
    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException() {}

    public DataAccessException(String s) {
        super(s);
    }

    public DataAccessException(String s, Throwable cause) {
        super(s, cause);
    }
}
