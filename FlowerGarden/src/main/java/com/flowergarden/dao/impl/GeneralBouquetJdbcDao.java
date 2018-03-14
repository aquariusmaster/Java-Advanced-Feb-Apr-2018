package com.flowergarden.dao.impl;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.domain.bouquet.MarriedBouquet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GeneralBouquetJdbcDao implements GeneralBouquetDao {

    private final JdbcHandler jdbcHandler;

    public GeneralBouquetJdbcDao(JdbcHandler jdbcHandler) {
        this.jdbcHandler = jdbcHandler;
    }

    @Override
    public void saveOrUpdate(GeneralBouquet bouquet) {

        switch (bouquet.getClass().getSimpleName()) {
            case "MarriedBouquet":
                saveOrUpdateMarriedBouquet((MarriedBouquet) bouquet);
                break;
            default:
                throw new RuntimeException("Cannot save bouquet: " + bouquet.getClass().getSimpleName() + " type does not support");
        }

    }

    @Override
    public GeneralBouquet findOne(Integer bouquetId) {
        final String sql = "SELECT * FROM flower WHERE id=?";
        List<GeneralBouquet> bouquets = extractBouquetListFromResultSet(jdbcHandler.executeSelect(sql, bouquetId));
        if (bouquets == null) return null;
        if (bouquets.size() > 1) {
            throw new RuntimeException("Not unique query result");
        }
        return bouquets.get(0);
    }

    @Override
    public List<GeneralBouquet> findAll(Integer bouquetId) {
        final String sql = "SELECT * FROM flower";

        return extractBouquetListFromResultSet(jdbcHandler.executeSelect(sql));
    }

    @Override
    public void delete(Integer bouquetId) {
        throw new UnsupportedOperationException();
    }

    private List<GeneralBouquet> extractBouquetListFromResultSet(ResultSet rs) {

        List<GeneralBouquet> bouquets = new ArrayList<>();

        try {

            while (rs.next()) {

                Integer id = rs.getInt("id");
                Float assemble_price = rs.getFloat("assemble_price");
                String type = rs.getString("name");

                switch (type) {

                    case "married":
                        MarriedBouquet marriedBouquet = new MarriedBouquet();
                        marriedBouquet.setId(id);
                        marriedBouquet.setAssemblePrice(assemble_price);

                        bouquets.add(marriedBouquet);
                        break;
                    default:
                        throw new RuntimeException("Cannot get bouquet: " + type + " type does not support");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                jdbcHandler.closeResultSet(rs);
                jdbcHandler.closeStatement(rs.getStatement());
                jdbcHandler.releaseConnection(rs.getStatement().getConnection());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bouquets;
    }

    private int saveOrUpdateMarriedBouquet(MarriedBouquet bouquet) {
        if (bouquet != null) {
            return updateBouquet(bouquet);
        } else {
            return createBouquet(bouquet);
        }
    }

    private int createBouquet(MarriedBouquet bouquet) {
        final String sql =
                "INSERT INTO flower (assemble_price, name) VALUES (?,?)";

        return jdbcHandler.executeUpdate(sql,
                bouquet.getAssemblePrice(),
                "married");

    }

    private int updateBouquet(MarriedBouquet bouquet) {
        final String sql =
                "UPDATE bouquet SET assemble_price=? WHERE id=?";

        return jdbcHandler.executeUpdate(sql,
                bouquet.getAssemblePrice(),
                bouquet.getId());
    }
}
