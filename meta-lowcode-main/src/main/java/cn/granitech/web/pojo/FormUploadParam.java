package cn.granitech.web.pojo;

import java.io.Serializable;

public class FormUploadParam implements Serializable {
    private String cloudStorage;
    private String cloudUploadToken;
    private String fileDownloadPrefix;
    private String fileUploadURL;
    private String picDownloadPrefix;
    private String picUploadURL;

    public String getCloudStorage() {
        return this.cloudStorage;
    }

    public void setCloudStorage(String cloudStorage2) {
        this.cloudStorage = cloudStorage2;
    }

    public String getCloudUploadToken() {
        return this.cloudUploadToken;
    }

    public void setCloudUploadToken(String cloudUploadToken2) {
        this.cloudUploadToken = cloudUploadToken2;
    }

    public String getPicUploadURL() {
        return this.picUploadURL;
    }

    public void setPicUploadURL(String picUploadURL2) {
        this.picUploadURL = picUploadURL2;
    }

    public String getFileUploadURL() {
        return this.fileUploadURL;
    }

    public void setFileUploadURL(String fileUploadURL2) {
        this.fileUploadURL = fileUploadURL2;
    }

    public String getPicDownloadPrefix() {
        return this.picDownloadPrefix;
    }

    public void setPicDownloadPrefix(String picDownloadPrefix2) {
        this.picDownloadPrefix = picDownloadPrefix2;
    }

    public String getFileDownloadPrefix() {
        return this.fileDownloadPrefix;
    }

    public void setFileDownloadPrefix(String fileDownloadPrefix2) {
        this.fileDownloadPrefix = fileDownloadPrefix2;
    }
}
