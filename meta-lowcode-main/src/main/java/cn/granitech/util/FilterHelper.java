package cn.granitech.util;

import cn.granitech.business.service.LayoutService;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.web.enumration.OperationEnum;
import cn.granitech.web.pojo.filter.Filter;
import cn.granitech.web.pojo.filter.FilterItem;
import cn.granitech.web.pojo.filter.SortFilter;
import cn.hutool.core.collection.CollUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FilterHelper {
    public static final String OR = "OR";
    public static final String AND = "AND";
    private static final String CLEAR_REGEX = "[AND|OR|0-9|\\s]";
    private static final String SORT_ASC = "ASC";
    private static final String TRUE = "true";

    public FilterHelper() {
    }

    public static String toSortFilter(List<SortFilter> sortFilters) {
        return ObjectUtils.isEmpty(sortFilters) ? "" : sortFilters.stream().map((sortFilter) -> {
            return String.format(" %s %s ", sortFilter.getFieldName(), sortFilter.getType() != null ? sortFilter.getType() : SORT_ASC);
        }).collect(Collectors.joining(", "));
    }

    public static String toFilter(Entity entity, Filter filter, Filter advFilter, Filter builtInFilter, Filter defaultFilter, String quickFilter) {
        String filterSql = toFilter(filter);
        String advFilterSql = toFilter(advFilter);
        String builtInFilterSql = toFilter(builtInFilter);
        String defaultFilterSql = toFilter(defaultFilter);
        String quickFilterSql = getQuickFilterSql(entity.getName(), quickFilter);
        return joinFilters("AND", defaultFilterSql, builtInFilterSql, advFilterSql, filterSql, quickFilterSql);
    }

    private static String getQuickFilterSql(String entityName, String quickFilter) {
        Field nameField;
        if (StringUtils.isBlank(quickFilter)) {
            return null;
        }
        List<Map<String, Object>> quickFilterFields = SpringHelper.getBean(LayoutService.class).getQuickFilterFields(entityName);
        if (CollUtil.isEmpty(quickFilterFields)) {
            return null;
        }
        StringBuilder quickFilterSql = new StringBuilder(" (");
        for (Map<String, Object> field : quickFilterFields) {
            String referTo = (String) field.get("referTo");
            if (referTo != null) {
                Entity entity = EntityHelper.getEntity(referTo);
                if (!(entity == null || (nameField = entity.getNameField()) == null)) {
                    quickFilterSql.append(field.get("name")).append(".").append(nameField.getName());
                }
            } else {
                quickFilterSql.append(field.get("name"));
            }
            quickFilterSql.append(" LIKE '%").append(quickFilter).append("%'").append(" OR ");
        }
        return quickFilterSql.append("false ) ").toString();
    }

    public static String joinFilters(String condition, String... filters) {
        List<String> filterList = Arrays.stream(filters).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        if (filterList.isEmpty()) {
            return "";
        } else {
            if (!"AND".equals(condition) && !"OR".equals(condition)) {
                condition = "AND";
            }

            String joinedFilters = String.join(") " + condition + " (", filterList);
            return "(" + joinedFilters + ")";
        }
    }

    public static String toFilter(Filter filter) {
        if (ObjectUtils.isEmpty(filter)) {
            return "";
        } else {
            String equation = validEquation(filter.getEquation());
            AssertHelper.isNotBlank(equation, "高级表达式格式不正确！");
            if (ObjectUtils.isEmpty(filter.getItems())) {
                return "";
            } else {
                StringBuilder filterStr = new StringBuilder(" ");
                if (!Objects.equals(equation, "OR") && !Objects.equals(equation, "AND")) {
                    assert equation != null;
                    String[] equationArr = equation.split(" ");

                    for (String str : equationArr) {
                        if (!str.equals("")) {
                            if (!str.equals("OR") && !str.equals("AND")) {
                                int index = new Integer(str);
                                if (filter.getItems().size() > index) {
                                    FilterItem item = filter.getItems().get(index);
                                    filterStr.append(OperationEnum.valueOf(item.getOp()).getSearch(item));
                                } else {
                                    filterStr.append(" true ");
                                }
                            } else {
                                filterStr.append(String.format(" %s ", str));
                            }
                        }
                    }

                } else {
                    ArrayList<String> filterItems = new ArrayList<>();
                    filter.getItems().forEach((itemx) -> {
                        filterItems.add(OperationEnum.valueOf(itemx.getOp()).getSearch(itemx));
                    });
                    filterStr.append(String.join(String.format(" %s ", equation), filterItems));
                }
                return filterStr.toString();
            }
        }
    }

    public static String validEquation(String equation) {
        if (StringUtils.isBlank(equation)) {
            return "OR";
        } else if (!"OR".equalsIgnoreCase(equation) && !"AND".equalsIgnoreCase(equation)) {
            String clearEquation = equation.toUpperCase().replace("OR", " OR ").replace("AND", " AND ").replaceAll("\\s+", " ").trim();
            equation = clearEquation;
            if (!clearEquation.startsWith("AND") && !clearEquation.startsWith("OR") && !clearEquation.endsWith("AND") && !clearEquation.endsWith("OR")) {
                if (!clearEquation.contains("()") && !clearEquation.contains("( )")) {
                    String[] var2 = clearEquation.split(" ");

                    for (String s : var2) {
                        String token = s;
                        token = token.replace("(", "");
                        token = token.replace(")", "");
                        if (NumberUtils.isNumber(token)) {
                            if (NumberUtils.toInt(token) > 10) {
                                return null;
                            }
                        } else if (!"AND".equals(token) && !"OR".equals(token) && !"(".equals(token) && !")".equals(token)) {
                            return null;
                        }
                    }

                    clearEquation = clearEquation.replaceAll(CLEAR_REGEX, "");

                    for (int i = 0; i < 20; ++i) {
                        clearEquation = clearEquation.replace("()", "");
                        if (clearEquation.length() == 0) {
                            return equation;
                        }
                    }

                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            return equation;
        }
    }
}
