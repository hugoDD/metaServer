package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.exception.MetadataSpacesException;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;

class J implements ConnectionCallback<Boolean> {
    PersistenceManagerImpl F;
    Entity ALLATORIxDEMO;
    ID D;

    J(PersistenceManagerImpl this$0, Entity var2, ID var3) {
        this.F = this$0;
        this.ALLATORIxDEMO = var2;
        this.D = var3;
    }

    public Boolean doInConnection(Connection conn) throws SQLException, DataAccessException {
        if (!SystemEntities.hasDeletedFlag(this.ALLATORIxDEMO.getName())) {
            throw new cn.granitech.variantorm.exception.DataAccessException("No deleted field of table: " + this.ALLATORIxDEMO.getPhysicalName());
        } else {
            String updateSql = "UPDATE {0} SET isDeleted = 0 WHERE {1}";
            String identifier = this.F.getDialect().getQuotedIdentifier(this.ALLATORIxDEMO.getPhysicalName());
            String idFieldName = this.F.getDialect().getQuotedIdentifier(this.ALLATORIxDEMO.getIdField().getPhysicalName()) + "=?";

            String sql = MessageFormat.format(updateSql, identifier, idFieldName);
            PreparedStatement a = null;

            try {
                PreparedStatement preparedStatement = a = conn.prepareStatement(sql);
                preparedStatement.setString(1, this.D.toString());
                if (preparedStatement.executeUpdate() != 1) {
                    throw new MetadataSpacesException("Restore Record Error: recordId is " + this.D.getId());
                }

                return true;
            } catch (SQLException e) {
                throw new cn.granitech.variantorm.exception.DataAccessException(e);
            } catch (Throwable throwable) {
                SqlHelper.closeStatement(a);
                throw throwable;
            } finally {

                SqlHelper.closeStatement(a);
            }

        }
    }
}
