package com.flowergarden.dao.impl;

import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.domain.flowers.Chamomile;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.domain.flowers.Rose;
import com.flowergarden.domain.flowers.Tulip;
import com.flowergarden.domain.properties.FreshnessInteger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class GeneralFlowerJdbcDao implements GeneralFlowerDao {

    private final JdbcHandler jdbcHandler;

    public GeneralFlowerJdbcDao(JdbcHandler jdbcHandler) {
        this.jdbcHandler = jdbcHandler;
    }

    @Override
    public void saveOrUpdate(GeneralFlower flower) {

        switch (flower.getClass().getSimpleName()) {
            case "Rose":
                saveOrUpdateRose((Rose) flower);
                break;
            case "Chamomile":
                saveOrUpdateChamomile((Chamomile) flower);
                break;
            case "Tulip":
                saveOrUpdateTulip((Tulip) flower);
                break;
            default:
                throw new RuntimeException("Cannot save flower: " + flower.getClass().getSimpleName() + " type does " +
                        "not support");
        }
    }

    @Override
    public GeneralFlower findOne(Integer flowerId) {

        final String sql = "SELECT * FROM flower WHERE id=?";
        ResultSet rs = jdbcHandler.executeSelect(sql, flowerId);
        List<GeneralFlower> resultList = this.extractFlowerListFromResultSet(rs);
        if (resultList == null) return null;
        if (resultList.size() > 1) {
            throw new RuntimeException("Not unique query result");
        }
        return resultList.get(0);
    }

    @Override
    public List<GeneralFlower> findAll() {

        final String sql = "SELECT * FROM flower";
        ResultSet rs = jdbcHandler.executeSelect(sql);
        return extractFlowerListFromResultSet(rs);

    }

    @Override
    public void delete(Integer flowerId) {
        throw new UnsupportedOperationException();
    }

    private int saveOrUpdateRose(Rose flower) {
        if (flower.getId() != null) {
            return updateRose(flower);
        } else {
            return createRose(flower);
        }
    }

    private int createRose(Rose flower) {
        final String sql =
                "INSERT INTO flower (price, length, freshness, spike, name, bouquet_id) " +
                        "VALUES (?,?,?,?,?,?)";

        return jdbcHandler.executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                flower.hasSpike(),
                "rose",
                flower.getBouquet() != null ? flower.getBouquet().getId() : null);
    }

    private int updateRose(Rose flower) {
        final String sql =
                "UPDATE flower SET price=?, length=?, freshness=?, spike=?, bouquet_id=? WHERE id=?";

        return jdbcHandler.executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                flower.hasSpike(),
                flower.getBouquet() != null ? flower.getBouquet().getId() : null,
                flower.getId());
    }

    private int saveOrUpdateTulip(Tulip flower) {
        if (flower.getId() != null) {
            return updateTulip(flower);
        } else {
            return createTulip(flower);
        }
    }

    private int createTulip(Tulip flower) {

        final String sql =
                "INSERT INTO flower (price, length, freshness, name, bouquet_id) " +
                "VALUES (?,?,?,?,?)";

        return jdbcHandler.executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                "tulip",
                flower.getBouquet() != null ? flower.getBouquet().getId() : null);
    }

    private int updateTulip(Tulip flower) {

        final String sql =
                "UPDATE flower SET price=?, length=?, freshness=?, bouquet_id=? WHERE id=?";

        return jdbcHandler.executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                flower.getBouquet() != null ? flower.getBouquet().getId() : null,
                flower.getId());

    }

    private int saveOrUpdateChamomile(Chamomile flower) {
        if (flower.getId() != null) {
            return updateChamomile(flower);
        } else {
            return createChamomile(flower);
        }
    }

    private int createChamomile(Chamomile flower) {

        final String sql =
                "INSERT INTO flower (price, length, freshness, petals, name, bouquet_id) " +
                        "VALUES (?,?,?,?,?,?)";

        return jdbcHandler.executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                flower.getPetals(),
                "chamomile",
                flower.getBouquet() != null ? flower.getBouquet().getId() : null);
    }

    private int updateChamomile(Chamomile flower) {
        final String sql =
                "UPDATE flower SET price=?, length=?, freshness=?, petals=?, bouquet_id=? WHERE id=?";

        return jdbcHandler.executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                flower.getPetals(),
                flower.getBouquet() != null ? flower.getBouquet().getId() : null,
                flower.getId());
    }

    private List<GeneralFlower> extractFlowerListFromResultSet(ResultSet rs) {

        List<GeneralFlower> flowers = new ArrayList<>();

        try {

            while (rs.next()) {

                Integer id = rs.getInt("id");
                Float price = rs.getFloat("price");
                Integer length = rs.getInt("length");
                FreshnessInteger freshness = new FreshnessInteger(rs.getInt("freshness"));
                String type = rs.getString("name");

                switch (type) {

                    case "rose":
                        Rose rose = new Rose();
                        rose.setId(id);
                        rose.setPrice(price);
                        rose.setLength(length);
                        rose.setFreshness(freshness);
                        Boolean spike = rs.getBoolean("spike");
                        rose.setSpike(spike);

                        flowers.add(rose);
                        break;
                    case "chamomile":
                        Chamomile chamomile = new Chamomile();
                        Integer petals = rs.getInt("petals");
                        chamomile.setPetals(petals);
                        chamomile.setId(id);
                        chamomile.setPrice(price);
                        chamomile.setLength(length);
                        chamomile.setFreshness(freshness);

                        flowers.add(chamomile);
                        break;
                    case "tulip":
                        Tulip tulip = new Tulip();
                        tulip.setId(id);
                        tulip.setPrice(price);
                        tulip.setLength(length);
                        tulip.setFreshness(freshness);

                        flowers.add(tulip);
                        break;
                    default:
                        throw new RuntimeException("Cannot get flower: " + type + " type does not support");
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

        return flowers;
    }

}
