package com.flowergarden.dao.impl;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.MissingFormatArgumentException;

public class JdbcHandler {

    private final String pathToDb;

    public JdbcHandler(String dbPath) {
        this.pathToDb = dbPath;
    }

    public Connection getConnection() {

        File fileToDB = new File(pathToDb);
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:sqlite:"+fileToDB.getCanonicalFile().toURI());

        } catch (SQLException | IOException e) {
            throw new RuntimeException("Could not get JDBC Connection", e);
        }
        return conn;
    }

    public void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public int executeUpdate(String query, Object ...props) {

        checkQuery(query, props);

        Connection conn = null;
        PreparedStatement stmt = null;
        int rowsChanged;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(query);

            fillStatementByProps(stmt, props);

            rowsChanged = stmt.executeUpdate();

        } catch (SQLException e) {
            closeStatement(stmt);
            stmt = null;
            releaseConnection(conn);
            conn = null;
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeStatement(stmt);
            releaseConnection(conn);
        }

        return rowsChanged;
    }

    private void fillStatementByProps(PreparedStatement stmt, Object ...props) throws SQLException {

        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                Object prop = props[i];

                if (prop instanceof Integer) {
                    stmt.setInt(i+1, (int) prop);
                } else if (prop instanceof Float || prop instanceof Double ) {
                    stmt.setFloat(i+1, (float) prop);
                } else if (prop instanceof String) {
                    stmt.setString(i+1, (String) prop);
                } else {
                    throw new MissingFormatArgumentException("Property type do not support");
                }
            }
        }

    }

    public ResultSet executeSelect(String sql, Object ...props) {

        checkQuery(sql, props);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            fillStatementByProps(stmt, props);

            resultSet = stmt.executeQuery();

        } catch (SQLException e) {
            closeStatement(stmt);
            stmt = null;
            releaseConnection(conn);
            conn = null;
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    private static void checkQuery(String query, Object ...props) {
        long count = query.chars().filter(ch -> ch == '?').count();
        int propsLength = props != null ? props.length : 0;
        if (count != propsLength) {
            throw new RuntimeException("Mismatch statement count");
        }

    }
}
