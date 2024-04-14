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

class UndeleteCallBack implements ConnectionCallback<Boolean> {
    PersistenceManagerImpl pm;
    Entity entity;
    ID id;

    UndeleteCallBack(PersistenceManagerImpl pm, Entity entity, ID id) {
        this.pm = pm;
        this.entity = entity;
        this.id = id;
    }

    public Boolean doInConnection(Connection conn) throws DataAccessException {
        if (!SystemEntities.hasDeletedFlag(this.entity.getName())) {
            throw new cn.granitech.variantorm.exception.DataAccessException("No deleted field of table: " + this.entity.getPhysicalName());
        } else {
            String updateSql = "UPDATE {0} SET isDeleted = 0 WHERE {1}";
            String identifier = this.pm.getDialect().getQuotedIdentifier(this.entity.getPhysicalName());
            String idFieldName = this.pm.getDialect().getQuotedIdentifier(this.entity.getIdField().getPhysicalName()) + "=?";

            String sql = MessageFormat.format(updateSql, identifier, idFieldName);
            PreparedStatement a = null;

            try {
                PreparedStatement preparedStatement = a = conn.prepareStatement(sql);
                preparedStatement.setString(1, this.id.toString());
                if (preparedStatement.executeUpdate() != 1) {
                    throw new MetadataSpacesException("Restore Record Error: recordId is " + this.id.getId());
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
