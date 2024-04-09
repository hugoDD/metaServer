package cn.granitech.variantorm.persistence.queryCompiler;


import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.metadata.MetadataManager;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.queryCompiler.antlr4.MySqlParser;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.pojo.Field;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;

import java.util.*;
import java.util.stream.Collectors;

public class SelectStatement {
    private final ParseTree tree;
    private final List<String> ruleNames;
    private Integer asIndex = 1;
    private SelectStatement superSelect;
    private final List<String> SUPER_TABLE = Arrays.stream(new String[]{"_outer", "_"}).collect(Collectors.toList());
    private final String PARAMETER_NAME = "pn_";
    private final StringBuffer sql = new StringBuffer();
    private MetadataManager metadata;
    private final List<String> fieldsList = new ArrayList<>();
    private Entity mainEntity;
    private String mainTableName;
    private Map<String, Field> fieldMap;
    private final Map<String, String> joinAsMap = new HashMap<>();
    private final StringBuffer joinSql = new StringBuffer();
    private final StringBuffer isDeleteSql = new StringBuffer();

    public SelectStatement(ParseTree tree, List<String> ruleNames) {
        this.tree = tree;
        this.ruleNames = ruleNames;
    }

    public SelectStatement(ParseTree tree, SelectStatement superSelect) {
        this.tree = tree;
        this.asIndex = superSelect.asIndex;
        this.ruleNames = superSelect.ruleNames;
        this.superSelect = superSelect;
    }

    public void compiler(MetadataManager metadata) {
        this.metadata = metadata;
        String tableName = this.getTableName();
        this.mainEntity = metadata.getEntity(tableName);
        this.mainTableName = this.createdAsName();
        List<ParseTree> fullColumnNames = this.getFullColumnNames();
        this.fieldMap = this.getFieldMap(fullColumnNames.stream().map(ParseTree::getText).collect(Collectors.toSet()));
        List<String> selectFullColumnNames = fullColumnNames.stream().filter(fullColumnName -> this.isSelectElement(fullColumnName.getParent())).map(ParseTree::getText).sorted().collect(Collectors.toList());
        this.putJoinMap(selectFullColumnNames);
        this.putIsDeleteSql(this.fieldMap, this.joinAsMap, this.isDeleteSql);
        this.treeCompiler(this.tree);
    }

    private void treeCompiler(ParseTree tree) {
        String nodeText = Trees.getNodeText(tree, this.ruleNames);
        if (nodeText.equals("selectStatement") && tree != this.tree) {
            SelectStatement select = new SelectStatement(tree, this);
            select.compiler(this.metadata);
            this.sql.append(select);
            return;
        }
        if (nodeText.equals("fullColumnName")) {
            this.fullColumnNameCompiler((ParserRuleContext)tree, this.fieldMap, this.joinAsMap);
            return;
        }
        if (nodeText.equals("tableSources")) {
            this.tableSourcesCompiler((ParserRuleContext)tree, this.joinSql);
            if (!this.HasWhereNode(tree.getParent())) {
                this.sql.append(" WHERE ").append(this.isDeleteSql).append(" (1=1) ");
            }
            return;
        }
        if (tree instanceof ParserRuleContext) {
            ParserRuleContext prc = (ParserRuleContext)tree;
            if (prc.children != null) {
                for (ParseTree child : prc.children) {
                    this.treeCompiler(child);
                }
                return;
            }
        }
        if (!nodeText.equals("(")) {
            this.sql.append(" ");
        }
        this.sql.append(nodeText);
        if (nodeText.equals("WHERE")) {
            this.sql.append(this.isDeleteSql);
        }
    }

    private boolean HasWhereNode(ParseTree parent) {
        if (this.tree instanceof ParserRuleContext) {
            ParserRuleContext prc = (ParserRuleContext)parent;
            if (prc.children != null) {
                for (ParseTree child : prc.children) {
                    String nodeText = Trees.getNodeText(child, this.ruleNames);
                    if (!nodeText.trim().equalsIgnoreCase("WHERE")) continue;
                    return true;
                }
            }
        }
        return false;
    }

    private void putIsDeleteSql(Map<String, Field> fieldMap, Map<String, String> joinAsMap, StringBuffer isDeleteSql) {
        if (SystemEntities.hasDeletedFlag(this.mainEntity.getName())) {
            this.putIsDeleteSql(isDeleteSql, this.mainTableName);
        }
        for (String key : joinAsMap.keySet()) {
            Field field = fieldMap.get(key);
            Entity entity = field.getReferTo().iterator().next();
            if (!SystemEntities.hasDeletedFlag(entity.getName())) continue;
            this.putIsDeleteSql(isDeleteSql, joinAsMap.get(key));
        }
    }

    private void putIsDeleteSql(StringBuffer isDeleteSql, String tableName) {
        isDeleteSql.append(" (").append(tableName).append(".isDeleted = 0 OR ").append(tableName).append(".isDeleted IS NULL)  AND ");
    }

    private void fullColumnNameCompiler(ParserRuleContext tree, Map<String, Field> fieldMap, Map<String, String> joinAsMap) {
        String text = tree.getText();
        if (text.startsWith(PARAMETER_NAME)) {
            this.sql.append(" :").append(text.substring(PARAMETER_NAME.length()));
            return;
        }
        Field field = fieldMap.get(text);
        if (field.getType() == FieldTypes.REFERENCELIST) {
            field = this.metadata.getEntity(field.getEntityCode()).getIdField();
        }
        this.sql.append(" ");
        if (!text.contains(".")) {
            this.sql.append(this.mainTableName).append(".").append(field.getPhysicalName());
        } else {
            int lastIndex = text.lastIndexOf(46);
            String table = text.substring(0, lastIndex);
            boolean flag = !this.SUPER_TABLE.contains(table);
            String tableAs = flag ? joinAsMap.get(table) : this.superSelect.mainTableName;
            this.sql.append(tableAs).append(".").append(field.getPhysicalName());
        }
        if (this.isSelectElement(tree.getParent())) {
            this.fieldsList.add(text);
            if (field.getType() == FieldTypes.REFERENCE && this.superSelect == null) {
                Entity entity = field.getReferTo().iterator().next();
                Field nameField = entity.getNameField() == null ? entity.getIdField() : entity.getNameField();
                this.sql.append(",").append(joinAsMap.get(text)).append(".").append(nameField.getPhysicalName());
                this.fieldsList.add(text + "." + nameField.getName());
            }
        }
    }

    private void tableSourcesCompiler(ParserRuleContext tree, StringBuffer joinSql) {
        this.sql.append(" ").append(this.mainEntity.getPhysicalName()).append(" AS ").append(this.mainTableName);
        this.sql.append(joinSql);
    }

    private void putJoinMap(List<String> fullColumnNames) {
        for (String fullColumnName : fullColumnNames) {
            this.putJoinMap(fullColumnName);
        }
    }

    private void putJoinMap(String fullColumnName) {
        String fieldName;
        String partnerAs;
        if (this.joinAsMap.get(fullColumnName) != null || this.SUPER_TABLE.contains(fullColumnName)) {
            return;
        }
        if (fullColumnName.startsWith(PARAMETER_NAME)) {
            return;
        }
        int lastIndex = fullColumnName.lastIndexOf(46);
        if (lastIndex >= 0) {
            String table = fullColumnName.substring(0, lastIndex);
            if (this.joinAsMap.get(table) == null) {
                this.putJoinMap(table);
            }
            partnerAs = this.joinAsMap.get(table);
            Entity entity = this.fieldMap.get(table).getReferTo().iterator().next();
            fieldName = entity.getField(fullColumnName.substring(lastIndex + 1)).getPhysicalName();
        } else {
            partnerAs = this.mainTableName;
            fieldName = this.mainEntity.getField(fullColumnName).getPhysicalName();
        }
        Field field = this.fieldMap.get(fullColumnName);
        if (field.getType() == FieldTypes.REFERENCE && this.superSelect == null) {
            String asName = this.createdAsName();
            Entity entity = field.getReferTo().iterator().next();
            this.joinAsMap.put(fullColumnName, asName);
            this.joinSql.append(" LEFT JOIN ").append(entity.getPhysicalName()).append(" AS ").append(asName).append(" ON ").append(asName).append(".").append(entity.getIdField().getPhysicalName()).append(" = ").append(partnerAs).append(".").append(fieldName);
        }
    }

    private Map<String, Field> getFieldMap(Set<String> fullColumnNames) {
        HashMap<String, Field> fieldMap = new HashMap<>();
        for (String fullColumnName : fullColumnNames) {
            if (fullColumnName.startsWith(PARAMETER_NAME)) continue;
            String[] names = fullColumnName.split("\\.");
            Entity entity = this.mainEntity;
            String nameText = "";
            for (String name : names) {
                nameText = nameText + name;
                if (this.SUPER_TABLE.contains(name)) {
                    entity = this.superSelect.mainEntity;
                    nameText = nameText + ".";
                    continue;
                }
                Field field = entity.getField(name);
                if (field.getType() == FieldTypes.REFERENCE) {
                    entity = field.getReferTo().iterator().next();
                }
                fieldMap.put(nameText, field);
                nameText = nameText + ".";
            }
        }
        return fieldMap;
    }

    private List<ParseTree> getFullColumnNames() {
        ArrayList<ParseTree> names = new ArrayList<>();
        this.putFullColumnNames(this.tree, names);
        return names;
    }

    private void putFullColumnNames(ParseTree tree, List<ParseTree> referenceNames) {
        String nodeText = Trees.getNodeText(tree, this.ruleNames);
        if (nodeText.equals("fullColumnName")) {
            referenceNames.add(tree);
            return;
        }
        if (nodeText.equals("selectStatement") && tree != this.tree) {
            return;
        }
        if (tree instanceof ParserRuleContext) {
            ParserRuleContext prc = (ParserRuleContext)tree;
            if (prc.children != null) {
                for (ParseTree child : prc.children) {
                    this.putFullColumnNames(child, referenceNames);
                }
            }
        }
    }

    private String getTableName() {
        MySqlParser.TableNameContext ruleContext = ((ParserRuleContext)this.tree).getRuleContext(MySqlParser.QuerySpecificationContext.class, 0).getRuleContext(MySqlParser.FromClauseContext.class, 0).getRuleContext(MySqlParser.TableSourcesContext.class, 0).getRuleContext(MySqlParser.TableSourceContext.class, 0).getRuleContext(MySqlParser.TableSourceItemContext.class, 0).getRuleContext(MySqlParser.TableNameContext.class, 0);
        return ruleContext.getText();
    }

    private boolean isSelectElement(ParseTree tree) {
        if (Trees.getNodeText(tree, this.ruleNames).equals("selectElement")) {
            return true;
        }
        if (tree.getParent() != null) {
            return this.isSelectElement(tree.getParent());
        }
        return false;
    }

    private String createdAsName() {
        Integer n = this.asIndex;
        this.asIndex = this.asIndex + 1;
        return "t_" + n;
    }

    public String toString() {
        return this.sql.toString();
    }

    public List<String> getFieldsList() {
        return this.fieldsList;
    }
}

