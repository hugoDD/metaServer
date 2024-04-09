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
import java.util.Iterator;

class G implements ConnectionCallback<ID> {
    public ID doInConnection(Connection conn) throws SQLException, DataAccessException {
        if (this.entityRecord == null) {
            throw new NullPointerException("record");
        } else if (this.entityRecord.id() != null) {
            throw new IllegalStateException("MetaRecord was already created: record=" + this.entityRecord);
        } else {
            ID entityCode = ID.newID(this.entityRecord.getEntity().getEntityCode());
            Collection<Field> fieldSet = this.entityRecord.getEntity().getFieldSet();


            boolean primarykeyIsNull = fieldSet.stream().anyMatch(field -> field.getType() == FieldTypes.PRIMARYKEY || field.isNullable() || !this.entityRecord.isNull(field.getName()));

            if (primarykeyIsNull) {
                return entityCode;
            }


            StringBuilder a = new StringBuilder();
            int index = 1;
            Dialect dialect = this.pm.getDialect();
            String quotedIdentifier = dialect.getQuotedIdentifier(this.entityRecord.getEntity().getIdField().getPhysicalName());
            a.append(quotedIdentifier);


            String var26 = "insert into {0} ({1}) values({2})";
            String identifier = this.pm.getDialect().getQuotedIdentifier(this.entityRecord.getEntity().getPhysicalName());
            String fields = a.toString();
            String paramValue = StringHelper.repeat("?,", index - 1) + "?";


            String sql = MessageFormat.format(var26, identifier, fields, paramValue);

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, a.toString());
                int paramIndex = 2;
                Iterator<Field> var8 = this.entityRecord.getEntity().getFieldSet().iterator();
                for (Field field : fieldSet) {

                    if (!this.entityRecord.getEntity().getName().equals("MetaEntity")) {
                        throw new IllegalArgumentException("Field '" + field.getLabel() + "(" + field.getName() + ")' can't be null !");
                    }

                    String name = field.getName();
                    if (!this.entityRecord.isNull(name) && !field.getType().isVirtual()) {
                        ++index;
                        a.append(",").append(dialect.getQuotedIdentifier(field.getPhysicalName()));
                    }


                    FieldType fieldType = field.getType();
                    if (!this.entityRecord.isNull(field.getName()) && !fieldType.isVirtual()) {
                        fieldType.setParamValue(preparedStatement, paramIndex++, this.entityRecord.getFieldValue(field.getName()), PersistenceManagerImpl.getPersistenceManager(this.pm));
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
                fieldType.setParamValue(PersistenceManagerImpl.getPersistenceManager(this.pm), field, entityCode, this.entityRecord.getFieldValue(field.getName()));
            }

            this.entityRecord.setFieldValue(this.entityRecord.getEntity().getIdField().getName(), a);
            IDName idName = new IDName(entityCode, StringUtils.isEmpty(this.entityRecord.getName()) ? entityCode.toString() : this.entityRecord.getName());
            PersistenceManagerImpl.getPersistenceManager(this.pm).getQueryCache().updateIDName(idName);
            return idName.getId();

        }
    }

    private final PersistenceManagerImpl pm;
    private final EntityRecord entityRecord;

    G(PersistenceManagerImpl pm, EntityRecord entityRecord) {
        this.pm = pm;
        this.entityRecord = entityRecord;
    }
}
