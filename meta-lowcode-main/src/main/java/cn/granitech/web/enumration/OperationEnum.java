package cn.granitech.web.enumration;

import cn.granitech.business.service.DepartmentService;
import cn.granitech.interceptor.CallerContext;
import cn.granitech.util.SpringHelper;
import cn.granitech.web.pojo.filter.FilterItem;

import java.util.List;

public enum OperationEnum {
    LK(" %s LIKE '%%%s%%' "),
    NLK(" %s NOT LIKE '%%%s%%' "),
    IN(" %s IN ('%s') ") {
        public String getSearch(FilterItem item) {
            String[] arr = item.getValue().split(",");
            item.setValue(String.join("','", arr));
            return super.getSearch(item);
        }
    },
    NIN(" %s NOT IN (%s) ") {
        public String getSearch(FilterItem item) {
            String[] arr = item.getValue().split(",");
            item.setValue(String.join("','", arr));
            return super.getSearch(item);
        }
    },
    EQ(" %s = '%s' "),
    NEQ(" %s != '%s' "),
    GT(" %s > '%s' "),
    LT(" %s < '%s' "),
    GE(" %s >= '%s' "),
    LE(" %s <= '%s' "),
    BW(" ( %s >= '%s' &&  %s <= '%s' ) ") {
        public String getSearch(FilterItem item) {
            return String.format(super.getSearch(), item.getFieldName(), item.getValue(), item.getFieldName(), item.getValue2());
        }
    },
    NL(" (%s IS NULL OR %s = '') ") {
        public String getSearch(FilterItem item) {
            return String.format(super.getSearch(), item.getFieldName(), item.getFieldName());
        }
    },
    NT(" (%s IS NOT NULL AND %s != '') ") {
        public String getSearch(FilterItem item) {
            return String.format(super.getSearch(), item.getFieldName(), item.getFieldName());
        }
    },
    BFD(" DATEDIFF(NOW(),%s) >= %s "),
    BFM(" TIMESTAMPDIFF(MONTH,DATE_FORMAT(%s, '%%Y-%%m-%%d'),DATE_FORMAT(NOW(), '%%Y-%%m-%%d'))  >= %s "),
    BFY(" (YEAR(NOW()) - YEAR(%s)) >= %s "),
    AFD(" DATEDIFF(NOW(),%s) <= -%s "),
    AFM(" TIMESTAMPDIFF(MONTH,DATE_FORMAT(%s, '%%Y-%%m-%%d'),DATE_FORMAT(NOW(), '%%Y-%%m-%%d'))  <= -%s "),
    AFY(" (YEAR(NOW()) - YEAR(%s)) <= -%s "),
    RED(" DATEDIFF(NOW(),%s) BETWEEN 0 AND %s "),
    REM(" TIMESTAMPDIFF(MONTH,DATE_FORMAT(%s, '%%Y-%%m-%%d'),DATE_FORMAT(NOW(), '%%Y-%%m-%%d'))  BETWEEN 0 AND %s "),
    REY(" (YEAR(NOW()) - YEAR(%s)) BETWEEN 0 AND %s "),
    FUD(" DATEDIFF(NOW(),%s) BETWEEN 0 AND -%s "),
    FUM(" TIMESTAMPDIFF(MONTH,DATE_FORMAT(%s, '%%Y-%%m-%%d'),DATE_FORMAT(NOW(), '%%Y-%%m-%%d'))  BETWEEN 0 AND -%s "),
    FUY(" (YEAR(NOW()) - YEAR(%s)) BETWEEN 0 AND -%s "),
    YTA(" DATEDIFF(NOW(),%s) = 1 "),
    TDA(" DATEDIFF(NOW(),%s) = 0 "),
    TTA(" DATEDIFF(NOW(),%s) = -1 "),
    CUW(" DATE_FORMAT(%s,'%%Y%%u') = DATE_FORMAT(NOW(),'%%Y%%u') "),
    CUM(" DATE_FORMAT(%s,'%%Y%%m') = DATE_FORMAT(NOW(),'%%Y%%m') "),
    CUQ(" CONCAT(YEAR(%s),QUARTER(%s)) = CONCAT(YEAR(NOW()),QUARTER(NOW())) ") {
        public String getSearch(FilterItem item) {
            return String.format(super.getSearch(), item.getFieldName(), item.getFieldName());
        }
    },
    CUY(" YEAR(%s) = YEAR(NOW()) "),
    SFU(" %s = '%s' ") {
        public String getSearch(FilterItem item) {
            CallerContext caller = SpringHelper.getBean(CallerContext.class);
            item.setValue(caller.getCallerId());
            return super.getSearch(item);
        }
    },
    SFB(" %s = '%s' ") {
        public String getSearch(FilterItem item) {
            CallerContext caller = SpringHelper.getBean(CallerContext.class);
            item.setValue(caller.getDepartmentId());
            return super.getSearch(item);
        }
    },
    SFD(" %s in (%s) ") {
        public String getSearch(FilterItem item) {
            DepartmentService dServer = SpringHelper.getBean(DepartmentService.class);
            CallerContext caller = SpringHelper.getBean(CallerContext.class);
            List<String> childrenDepartment = dServer.getChildrenDepartment(caller.getDepartmentId());
            item.setValue("'" + String.join("','", childrenDepartment) + "'");
            return super.getSearch(item);
        }
    },
    REF(" exists (select objectId from ReferenceListMap where fieldName like '%s' and objectId=_.%s and toId='%s') ") {
        public String getSearch(FilterItem item) {
            return String.format(super.getSearch(), item.getFieldName(), item.getValue(), item.getValue2());
        }
    };

    private static final String MONTH_DIFF = " TIMESTAMPDIFF(MONTH,DATE_FORMAT(%s, '%%Y-%%m-%%d'),DATE_FORMAT(NOW(), '%%Y-%%m-%%d')) ";
    private final String search;

    OperationEnum(String search) {
        this.search = search;
    }

    private final String getSearch() {
        return this.search;
    }

    public String getSearch(FilterItem item) {
        return String.format(this.search, item.getFieldName(), item.getValue());
    }
}
