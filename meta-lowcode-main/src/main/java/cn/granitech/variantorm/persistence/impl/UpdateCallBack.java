package cn.granitech.variantorm.persistence.impl;

import cn.granitech.variantorm.exception.MetadataSpacesException;
import cn.granitech.variantorm.metadata.FieldType;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.metadata.fieldtype.FieldTypes;
import cn.granitech.variantorm.persistence.EntityRecord;
import cn.granitech.variantorm.pojo.Field;
import cn.granitech.variantorm.pojo.IDName;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

public class UpdateCallBack implements ConnectionCallback<Boolean> {
    private final PersistenceManagerImpl pm;
    private final EntityRecord entityRecord;
    UpdateCallBack(PersistenceManagerImpl pm, EntityRecord entityRecord) {
        this.pm = pm;
        this.entityRecord = entityRecord;
    }

    public Boolean doInConnection(Connection conn) throws SQLException, DataAccessException {
        if (this.entityRecord == null) {
            throw new NullPointerException("record");
        } else if (this.entityRecord.id() == null) {
            throw new IllegalArgumentException("Record's id should not be null!");
        } else if (!this.entityRecord.isModified()) {
            throw new IllegalArgumentException("Record is unmodified!");
        } else {
            Collection<Field> fieldSet = this.entityRecord.getEntity().getFieldSet();
            StringBuilder a = new StringBuilder();
            for (Field field : fieldSet){
                String fieldName = field.getName();
                if (this.entityRecord.isModified(fieldName) && !field.isNullable() && this.entityRecord.isNull(fieldName)) {
                    throw new IllegalArgumentException("Field '"+field.getLabel()+"("+fieldName+")' can't be null!");
                }
                FieldType fieldType = field.getType();
                if (this.entityRecord.isModified(fieldName) && !fieldType.isVirtual() && fieldType != FieldTypes.PRIMARYKEY) {
                    if (fieldName.length() > 0) {
                        a.append(",");
                    }

                    a.append(this.pm.getDialect().getQuotedIdentifier(field.getPhysicalName())).append("=?");
                }



            }




            if (!a.toString().trim().equals("")) {
                String updateSql = "update {0} set {1} where {2} ";
                String identifier = this.pm.getDialect().getQuotedIdentifier(this.entityRecord.getEntity().getPhysicalName());
                String updateField = a.toString();
                String idFieldIdentifier  =  this.pm.getDialect().getQuotedIdentifier(this.entityRecord.getEntity().getIdField().getPhysicalName()) + "=?";

                String sql = MessageFormat.format(updateSql, identifier,updateField,idFieldIdentifier);

                PreparedStatement preparedStatement = null;
                try {
                    try {
                        preparedStatement = conn.prepareStatement(sql);
                        int parameterIndex = 1;
                        Iterator<Field> var22 = this.entityRecord.getEntity().getFieldSet().iterator();

                        while(true) {
                            if (!var22.hasNext()) {
                                ID var25 = this.entityRecord.id();
                                ++parameterIndex;
                                preparedStatement.setString(parameterIndex, var25.toString());
                                if (preparedStatement.executeUpdate() == 1) {
                                    break;
                                }

                                throw new MetadataSpacesException( "Update Record Error: "+ this.entityRecord);
                            }

                          Field  field = var22.next();
                            String fieldName = field.getName();
                            FieldType fieldType = field.getType();
                            if (this.entityRecord.isModified(fieldName) && !fieldType.isVirtual() && fieldType != FieldTypes.PRIMARYKEY) {
                                fieldType.setParamValue(preparedStatement, parameterIndex++, this.entityRecord.getFieldValue(fieldName), PersistenceManagerImpl.getPersistenceManager(this.pm));
                            }
                        }
                    } catch (SQLException var13) {
                        var13.printStackTrace();
                        throw new cn.granitech.variantorm.exception.DataAccessException(var13);
                    }
                } catch (Throwable var14) {
                    SqlHelper.closeStatement(preparedStatement);
                    throw var14;
                }

                SqlHelper.closeStatement(preparedStatement);
            }

            for (Field field : this.entityRecord.getEntity().getVirtualFieldSet()) {
                if (this.entityRecord.isModified(field.getName())) {
                    System.err.println("Modified Record: " + field.getName());
                    field.getType().setParamValue(PersistenceManagerImpl.getPersistenceManager(this.pm),
                            field, this.entityRecord.id(),
                            this.entityRecord.getFieldValue(field.getName()));
                }
            }
            Field field = this.entityRecord.getEntity().getNameField();
            if (field   != null && this.entityRecord.isModified(field.getName())) {
                IDName idName = new IDName(this.entityRecord.id(), this.entityRecord.getName());
                PersistenceManagerImpl.getPersistenceManager(this.pm).getQueryCache().updateIDName(idName);
            }

            return true;
        }
    }
}
