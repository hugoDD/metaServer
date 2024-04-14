package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.persistence.dialect.Dialect;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.util.StringHelper;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

class InsertCallBack implements ConnectionCallback<ID> {
    public ID doInConnection(Connection conn) throws SQLException, DataAccessException {
        if (this.entityRecord == null) {
            throw new NullPointerException("record");
        } else if (this.entityRecord.id() != null) {
            throw new IllegalStateException("MetaRecord was already created: record=" + this.entityRecord);
        } else {
            ID newID = ID.newID(this.entityRecord.getEntity().getEntityCode());
            Collection<Field> fieldSet = this.entityRecord.getEntity().getFieldSet();


            Optional<Field> matchField = fieldSet.stream().filter(field -> !(field.getType() == FieldTypes.PRIMARYKEY || field.isNullable() || !this.entityRecord.isNull(field.getName())))
                    .findFirst();
//
            if (matchField.isPresent()) {
                if (!this.entityRecord.getEntity().getName().equals("MetaEntity")) {
                    Field field = matchField.get();
                    throw new IllegalArgumentException("Field '" + field.getLabel() + "(" + field.getName() + ")' can't be null !");
                }
            }
            Dialect dialect = this.pm.getDialect();

            List<String> fieldList = fieldSet.stream().filter(field -> !this.entityRecord.isNull(field.getName()))
                    .filter(field -> !field.getType().isVirtual())
                    .map(Field::getPhysicalName)
                    .map(dialect::getQuotedIdentifier)
                    .collect(Collectors.toList());
//                    .collect(Collectors.joining(","));

            int index = fieldList.size();
            String idPhysicalName = this.entityRecord.getEntity().getIdField().getPhysicalName();
            fieldList.add(0,dialect.getQuotedIdentifier(idPhysicalName));
            String fields = String.join(",",fieldList);



            String var26 = "insert into {0} ({1}) values({2})";
            String identifier = this.pm.getDialect().getQuotedIdentifier(this.entityRecord.getEntity().getPhysicalName());
//            String fields = a.toString();
            String paramValue = StringHelper.repeat("?,", index ) + "?";


            String sql = MessageFormat.format(var26, identifier, fields, paramValue);

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, newID.toString());
                int paramIndex = 2;
                for (Field field : fieldSet) {

                    FieldType fieldType = field.getType();
                    if (!this.entityRecord.isNull(field.getName()) && !fieldType.isVirtual()) {
                        fieldType.setParamValue(preparedStatement, paramIndex++, this.entityRecord.getFieldValue(field.getName()), pm);
                    }
                }

                System.out.println(paramIndex);
                preparedStatement.executeUpdate();


            } finally {
                SqlHelper.closeStatement(preparedStatement);
            }

            for (Field field : this.entityRecord.getEntity().getVirtualFieldSet()) {
                FieldType fieldType = field.getType();
                System.out.println(fieldType.getName());
                System.out.println("isVirtual: " + fieldType.isVirtual());
                fieldType.setParamValue(this.pm, field, newID, this.entityRecord.getFieldValue(field.getName()));
            }

            this.entityRecord.setFieldValue(this.entityRecord.getEntity().getIdField().getName(), newID);
            IDName idName = new IDName(newID, StringUtils.isEmpty(this.entityRecord.getName()) ? newID.toString() : this.entityRecord.getName());
            this.pm.getQueryCache().updateIDName(idName);
            return idName.getId();

        }
    }

    private final PersistenceManagerImpl pm;
    private final EntityRecord entityRecord;

    InsertCallBack(PersistenceManagerImpl pm, EntityRecord entityRecord) {
        this.pm = pm;
        this.entityRecord = entityRecord;
    }
}
