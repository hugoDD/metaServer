package cn.granitech.variantorm.persistence.impl;


import cn.granitech.variantorm.constant.SystemEntities;
import cn.granitech.variantorm.metadata.ID;
import cn.granitech.variantorm.pojo.Entity;
import cn.granitech.variantorm.util.sql.SqlHelper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;

class LogicDeleteCallBack implements ConnectionCallback<Boolean> {
    public Boolean doInConnection(Connection conn) throws SQLException, DataAccessException {
        String delSql = "DELETE FROM {0} WHERE {1}";
        String identifier = this.pm.getDialect().getQuotedIdentifier(this.entity.getPhysicalName());
        String idFieldName = this.pm.getDialect().getQuotedIdentifier(this.entity.getIdField().getPhysicalName()) + "=?";

        String sql = MessageFormat.format(delSql, identifier, idFieldName);
        if (SystemEntities.hasDeletedFlag(this.entity.getName())) {
            delSql = "UPDATE {0} SET isDeleted = 1 WHERE {1} and (isDeleted = 0 or isDeleted IS NULL)";
            sql = MessageFormat.format(delSql, identifier, idFieldName);
        }

        PreparedStatement preparedStatement = null;

        try {
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, this.id.getId());
                if (preparedStatement.executeUpdate() != 1) {
                    return false;
                }

                this.pm.getQueryCache().deleteIDName(this.id.toString());

        } catch (Throwable e) {
            SqlHelper.closeStatement(preparedStatement);
            throw e;
        } finally {
            SqlHelper.closeStatement(preparedStatement);

        }

        return true;
    }

    private final PersistenceManagerImpl pm;
    private final Entity entity;
    private final ID id;

    LogicDeleteCallBack(PersistenceManagerImpl persistenceManager, Entity entity, ID id) {
        this.pm = persistenceManager;
        this.entity = entity;
        this.id = id;
    }
}

