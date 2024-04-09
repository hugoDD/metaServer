package cn.granitech.variantorm.exception;

public class LicenseException extends RuntimeException {
    private String deviceNo;

    public LicenseException(Throwable cause) {
        super(cause);
    }

    public String getDeviceNo() {
        return this.deviceNo;
    }

    public LicenseException() {}

    public LicenseException(String message) {
        super(message);
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }
}
