package com.flowergarden.dao.impl;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.MissingFormatArgumentException;

@Component("jdbcHandler")
public class JdbcHandler {

    private DataSource dataSource;

    public JdbcHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {

        try {
            return dataSource.getConnection();

        } catch (SQLException e) {
            throw new RuntimeException("Could not get JDBC Connection", e);
        }
    }

    public static void releaseConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                //close silently
                e.printStackTrace();
            }
        }

    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                //close silently
                e.printStackTrace();
            }
        }

    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                //close silently
                e.printStackTrace();
            }
        }

    }

    public static void closeResultSetAndStatementAndConnection(ResultSet rs) {

        try {
            closeResultSet(rs);
            closeStatement(rs.getStatement());
            releaseConnection(rs.getStatement().getConnection());
        } catch (SQLException e) {
            //close silently
            e.printStackTrace();
        }

    }

    public int executeUpdate(String query, Object... props) {

        checkQuery(query, props);

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)){

            fillStatementByProps(stmt, props);

            return stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ResultSet executeSelect(String sql, Object... props) {

        checkQuery(sql, props);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);

            fillStatementByProps(stmt, props);

            return stmt.executeQuery();

        } catch (SQLException e) {
            closeStatement(stmt);
            stmt = null;
            releaseConnection(conn);
            conn = null;
            throw new RuntimeException(e);
        }

    }

    private static void checkQuery(String query, Object... props) {
        long count = query.chars().filter(ch -> ch == '?').count();
        int propsLength = props != null ? props.length : 0;
        if (count != propsLength) {
            throw new RuntimeException("Mismatch statement count");
        }

    }

    private void fillStatementByProps(PreparedStatement stmt, Object... props) throws SQLException {

        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                Object prop = props[i];
                if (prop instanceof Integer) {
                    stmt.setInt(i + 1, (int) prop);
                } else if (prop instanceof Float || prop instanceof Double) {
                    stmt.setFloat(i + 1, (float) prop);
                } else if (prop instanceof String) {
                    stmt.setString(i + 1, (String) prop);
                } else if (prop instanceof Boolean) {
                    stmt.setBoolean(i + 1, (boolean) prop);
                } else if (prop == null) {
                    stmt.setObject(i + 1, null);
                } else {
                    throw new MissingFormatArgumentException("Property type do not support");
                }
            }
        }

    }
}
