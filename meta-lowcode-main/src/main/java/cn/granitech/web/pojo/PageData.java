package cn.granitech.web.pojo;

import cn.granitech.variantorm.pojo.Pagination;

import java.io.Serializable;
import java.util.List;

public class PageData<T> extends Pagination implements Serializable {
    private static final long serialVersionUID = 1;
    private List<T> pageData;

    private PageData() {
    }

    public PageData(Pagination pagination) {
        setPageNo(Integer.valueOf(pagination.getPageNo() == null ? 1 : pagination.getPageNo().intValue()));
        setPageSize(Integer.valueOf(pagination.getPageSize() == null ? 10 : pagination.getPageSize().intValue()));
    }

    public List<T> getPageData() {
        return this.pageData;
    }

    public void setPageData(List<T> pageData2) {
        this.pageData = pageData2;
    }
}
