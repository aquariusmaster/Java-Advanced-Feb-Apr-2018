package com.flowergarden.dao.impl;

import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.flowers.Tulip;
import com.flowergarden.properties.FreshnessInteger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

public class GeneralFlowerDaoJdbcImpl implements GeneralFlowerDao {

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
        }
    }

    @Override
    public GeneralFlower findOne(Integer flowerId) {

        final String sql = "SELECT * FROM flower WHERE id=?";
        ResultSet rs = executeSelect(sql, flowerId);
        List<GeneralFlower> resultList = this.extractFlowerListFromResultSet(rs);
        if (resultList.size() > 1) {
            throw new RuntimeException("Not unique query result");
        }
        return resultList.get(0);
    }

    @Override
    public List<GeneralFlower> findAll() {

        final String sql = "SELECT * FROM flower";
        ResultSet rs = executeSelect(sql, null);
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
                "INSERT INTO flower (price, lenght, freshness, spike, name) " +
                        "VALUES (?,?,?,?,?)";

        return executeUpdate(sql,
                flower.getPrice(), flower.getLenght(),
                flower.getFreshness().getFreshness(), flower.hasSpike(), "rose");
    }

    private int updateRose(Rose flower) {
        final String sql =
                "UPDATE flower SET price=?, lenght=?, freshness=?, spike=? WHERE id=?";

        return executeUpdate(sql,
                flower.getPrice(), flower.getLenght(), flower.getFreshness().getFreshness(),
                flower.hasSpike(), flower.getId());
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
                "INSERT INTO flower (price, lenght, freshness, name) " +
                "VALUES (?,?,?,?)";

        return executeUpdate(sql,
                flower.getPrice(), flower.getLenght(),
                flower.getFreshness().getFreshness(), "tulip");
    }

    private int updateTulip(Tulip flower) {

        final String sql =
                "UPDATE flower SET price=?, lenght=?, freshness=? WHERE id=?";

        return executeUpdate(sql,
                flower.getPrice(), flower.getLenght(),
                flower.getFreshness().getFreshness(), flower.getId());

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
                "INSERT INTO flower (price, lenght, freshness, petals, name) " +
                        "VALUES (?,?,?,?,?)";

        return executeUpdate(sql,
                flower.getPrice(), flower.getLenght(),
                flower.getFreshness().getFreshness(), flower.getPetals(), "chamomile");
    }

    private int updateChamomile(Chamomile flower) {
        final String sql =
                "UPDATE flower SET price=?, lenght=?, freshness=?, petals=? WHERE id=?";

        return executeUpdate(sql,
                flower.getPrice(), flower.getLenght(), flower.getFreshness().getFreshness(),
                flower.getPetals(), flower.getId());
    }

    private List<GeneralFlower> extractFlowerListFromResultSet(ResultSet rs) {

        List<GeneralFlower> flowers = new ArrayList<>();

        try {

            while (rs.next()) {

                GeneralFlower flower = null;

                Integer id = rs.getInt("id");
                Float price = rs.getFloat("price");
                Integer length = rs.getInt("lenght");
                String name = rs.getString("name");
                FreshnessInteger freshness = new FreshnessInteger(rs.getInt("freshness"));

                switch (name) {

                    case "rose":
                        Rose rose = new Rose();
                        rose.setId(id);
                        rose.setPrice(price);
                        rose.setLenght(length);
                        rose.setFreshness(freshness);
                        Boolean spike = rs.getBoolean("spike");
                        rose.setSpike(spike);
                        flower = rose;
                        break;
                    case "chamomile":
                        Chamomile chamomile = new Chamomile();
                        Integer petals = rs.getInt("petals");
                        chamomile.setPetals(petals);
                        chamomile.setId(id);
                        chamomile.setPrice(price);
                        chamomile.setLenght(length);
                        chamomile.setFreshness(freshness);
                        flower = chamomile;
                        break;
                    case "tulip":
                        Tulip tulip = new Tulip();
                        tulip.setId(id);
                        tulip.setPrice(price);
                        tulip.setLenght(length);
                        tulip.setFreshness(freshness);
                        flower = tulip;
                        break;
                }

                flowers.add(flower);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return flowers;
    }

    private int executeUpdate(String query, Object ...props) {

        checkQuery(query, props);

        Connection conn = null;
        PreparedStatement stmt = null;
        int rowsChanged;

        try {
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement(query);

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

            rowsChanged = stmt .executeUpdate();

        } catch (SQLException e) {
            DBUtils.closeStatement(stmt);
            stmt = null;
            DBUtils.releaseConnection(conn);
            conn = null;
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            DBUtils.closeStatement(stmt);
            DBUtils.releaseConnection(conn);
        }

        return rowsChanged;
    }

    private ResultSet executeSelect(String sql, Object ...props) {

        checkQuery(sql, props);

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            conn = DBUtils.getConnection();
            stmt = conn.prepareStatement(sql);

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

            resultSet = stmt.executeQuery();

        } catch (SQLException e) {
            DBUtils.closeStatement(stmt);
            stmt = null;
            DBUtils.releaseConnection(conn);
            conn = null;
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            DBUtils.closeStatement(stmt);
            DBUtils.releaseConnection(conn);
        }

        return resultSet;
    }

    private void checkQuery(String query, Object ...props) {
        long count = query.chars().filter(ch -> ch == '?').count();
        if (count != props.length) {
            throw new RuntimeException("Mismatch statement count");
        }

    }

}
