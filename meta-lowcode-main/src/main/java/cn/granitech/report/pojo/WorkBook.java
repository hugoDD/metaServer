package cn.granitech.report.pojo;


public class WorkBook {
    private String gridKey;
    private String title;
    private Integer platform;
    private String entityKey;
    private String sheets;

    public WorkBook(String gridKey, String title, String entityKey, Integer platform) {
        this.gridKey = gridKey;
        this.title = title;
        this.entityKey = entityKey;
        this.platform = platform;
        this.sheets = "[{\"name\": \"sheet1\"}]";
    }

    public String getGridKey() {
        return this.gridKey;
    }

    public void setGridKey(String gridKey) {
        this.gridKey = gridKey;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPlatform() {
        return this.platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getEntityKey() {
        return this.entityKey;
    }

    public void setEntityKey(String entityKey) {
        this.entityKey = entityKey;
    }

    public String getSheets() {
        return this.sheets;
    }

    public void setSheets(String sheets) {
        this.sheets = sheets;
    }
}



