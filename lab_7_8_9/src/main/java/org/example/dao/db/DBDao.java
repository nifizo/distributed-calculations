package org.example.dao.db;

import org.example.dao.CrudDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class DBDao<T, Id> implements CrudDao<T, Id> {
    protected final String tableName;
    protected Connection con;

    protected DBDao(Connection con, String tableName) throws SQLException {
        this.tableName = tableName;
        this.con = con;

        createTableIfNotExists();
    }

    protected abstract void createTableIfNotExists() throws SQLException;
}
