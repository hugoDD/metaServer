package cn.granitech.web.pojo;

import cn.granitech.util.EntityHelper;
import cn.granitech.util.FilterHelper;
import cn.granitech.util.MetadataHelper;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Pagination;
import cn.granitech.variantorm.pojo.QuerySchema;
import cn.granitech.web.pojo.filter.Filter;
import cn.granitech.web.pojo.filter.FilterItem;
import cn.granitech.web.pojo.filter.SortFilter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListQueryRequestBody {
    private Filter advFilter;
    private Filter builtInFilter;
    private Filter defaultFilter;
    private String fieldsList;
    private Filter filter;
    private String mainEntity;
    private Integer pageNo;
    private Integer pageSize;
    private String quickFilter;
    private List<SortFilter> sortFields;

    public QuerySchema querySchema() {
        Entity entity = EntityHelper.getEntity(this.mainEntity);
        QuerySchema querySchema = new QuerySchema();
        querySchema.setMainEntity(this.mainEntity);
        querySchema.setSelectFields(MetadataHelper.entityFieldFilter(entity, this.fieldsList));
        if (ObjectUtil.isNotNull(this.advFilter) && CollUtil.isNotEmpty(this.advFilter.getItems())) {
            List<FilterItem> filterItems = this.advFilter.getItems().stream()
                    .filter((item) -> StringUtils.isNotBlank(MetadataHelper.entityFieldFilter(entity, item.getFieldName())))
                    .collect(Collectors.toList());
            this.advFilter.setItems(filterItems);
        }

        querySchema.setFilter(FilterHelper.toFilter(entity, this.getFilter(), this.advFilter, this.builtInFilter, this.defaultFilter, this.quickFilter));
        if (CollUtil.isNotEmpty(this.sortFields)) {
            this.sortFields = this.sortFields.stream().filter((sortField) -> {
                return StringUtils.isNotBlank(MetadataHelper.entityFieldFilter(entity, sortField.getFieldName()));
            }).collect(Collectors.toList());
        }

        querySchema.setSort(FilterHelper.toSortFilter(this.sortFields));
        return querySchema;
    }

    public Filter getDefaultFilter() {
        return this.defaultFilter;
    }

    public void setDefaultFilter(Filter defaultFilter2) {
        this.defaultFilter = defaultFilter2;
    }

    public Pagination pagination() {
        if (this.pageNo == null || this.pageSize == null) {
            return null;
        }
        return new Pagination(this.pageNo, this.pageSize);
    }

    public Filter getBuiltInFilter() {
        return this.builtInFilter;
    }

    public void setBuiltInFilter(Filter builtInFilter2) {
        this.builtInFilter = builtInFilter2;
    }

    public Filter getAdvFilter() {
        return this.advFilter;
    }

    public void setAdvFilter(Filter advFilter2) {
        this.advFilter = advFilter2;
    }

    public String getQuickFilter() {
        return this.quickFilter;
    }

    public void setQuickFilter(String quickFilter2) {
        this.quickFilter = quickFilter2;
    }

    public String getMainEntity() {
        return this.mainEntity;
    }

    public void setMainEntity(String mainEntity2) {
        this.mainEntity = mainEntity2;
    }

    public String getFieldsList() {
        return this.fieldsList;
    }

    public void setFieldsList(String fieldsList2) {
        this.fieldsList = fieldsList2;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public void setFilter(Filter filter2) {
        this.filter = filter2;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize2) {
        this.pageSize = pageSize2;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(Integer pageNo2) {
        this.pageNo = pageNo2;
    }

    public List<SortFilter> getSortFields() {
        return this.sortFields;
    }

    public void setSortFields(List<SortFilter> sortFields2) {
        this.sortFields = sortFields2;
    }
}
