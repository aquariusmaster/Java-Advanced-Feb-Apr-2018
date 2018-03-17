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

        if (flower.getId() != null) {
            update(flower);
        } else {
            create(flower);
        }

    }

    @Override
    public GeneralFlower findOne(Integer flowerId) {

        final String sql = "SELECT * FROM flower WHERE id=?";
        ResultSet rs = jdbcHandler.executeSelect(sql, flowerId);
        List<GeneralFlower> resultList = this.extractFlowerListFromResultSet(rs);
        if (resultList.isEmpty()) return null;
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

    @Override
    public List<GeneralFlower> findAllByBouquetId(Integer bouquetId) {
        final String sql = "SELECT * FROM flower WHERE bouquet_id=?";
        ResultSet rs = jdbcHandler.executeSelect(sql, bouquetId);
        return extractFlowerListFromResultSet(rs);
    }

    private int create(GeneralFlower flower) {
        final String sql =
                "INSERT INTO flower (name, length, freshness, price, petals, spike, bouquet_id) " +
                        "VALUES (?,?,?,?,?,?,?)";

        Integer petals = null;
        if (flower instanceof Chamomile) {
            petals = ((Chamomile) flower).getPetals();
        }

        return jdbcHandler.executeUpdate(sql,
                flower.getClass().getSimpleName().toLowerCase(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                flower.getPrice(),
                petals,
                flower instanceof Rose ? true : null,
                flower.getBouquet() != null ? flower.getBouquet().getId() : null);
    }

    private int update(GeneralFlower flower) {
        final String sql =
                "UPDATE flower SET length=?, freshness=?, price=?, petals=?, spike=?, bouquet_id=? WHERE id=?";

        Integer petals = null;
        if (flower instanceof Chamomile) {
            Chamomile chamomile = (Chamomile) flower;
            petals = chamomile.getPetals();
        }

        return jdbcHandler.executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                petals,
                flower instanceof Rose ? true : null,
                flower.getBouquet() != null ? flower.getBouquet().getId() : null,
                flower.getId());
    }


    private List<GeneralFlower> extractFlowerListFromResultSet(ResultSet rs) {

        try {

            List<GeneralFlower> flowers = new ArrayList<>();

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

            return flowers;

        } catch (SQLException e) {
            throw new RuntimeException("Exception while trying extract Flower list", e);
        } finally {
            jdbcHandler.closeResultSetAndStatementAndConnection(rs);
        }

    }

}
