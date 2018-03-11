package com.flowergarden.dao.impl;

import com.flowergarden.dao.FlowerDao;
import com.flowergarden.domain.flowers.Chamomile;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.domain.flowers.Rose;
import com.flowergarden.domain.flowers.Tulip;
import com.flowergarden.domain.properties.FreshnessInteger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

public class FlowerDaoJdbcImpl implements FlowerDao {

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
                "INSERT INTO flower (price, length, freshness, spike, name, bouquet_id) " +
                        "VALUES (?,?,?,?,?,?)";

        return executeUpdate(sql,
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

        return executeUpdate(sql,
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

        return executeUpdate(sql,
                flower.getPrice(),
                flower.getLength(),
                flower.getFreshness().getFreshness(),
                "tulip",
                flower.getBouquet() != null ? flower.getBouquet().getId() : null);
    }

    private int updateTulip(Tulip flower) {

        final String sql =
                "UPDATE flower SET price=?, length=?, freshness=?, bouquet_id=? WHERE id=?";

        return executeUpdate(sql,
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

        return executeUpdate(sql,
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

        return executeUpdate(sql,
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

                GeneralFlower flower = null;

                Integer id = rs.getInt("id");
                Float price = rs.getFloat("price");
                Integer length = rs.getInt("length");
                String name = rs.getString("name");
                FreshnessInteger freshness = new FreshnessInteger(rs.getInt("freshness"));

                switch (name) {

                    case "rose":
                        Rose rose = new Rose();
                        rose.setId(id);
                        rose.setPrice(price);
                        rose.setLength(length);
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
                        chamomile.setLength(length);
                        chamomile.setFreshness(freshness);
                        flower = chamomile;
                        break;
                    case "tulip":
                        Tulip tulip = new Tulip();
                        tulip.setId(id);
                        tulip.setPrice(price);
                        tulip.setLength(length);
                        tulip.setFreshness(freshness);
                        flower = tulip;
                        break;
                }

                flowers.add(flower);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                DBUtils.releaseConnection(rs.getStatement().getConnection());
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

            rowsChanged = stmt.executeUpdate();

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

            resultSet = stmt.executeQuery();

        } catch (SQLException e) {
            DBUtils.closeStatement(stmt);
            stmt = null;
            DBUtils.releaseConnection(conn);
            conn = null;
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
//            DBUtils.closeStatement(stmt);
//            DBUtils.releaseConnection(conn);
        }

        System.out.println(resultSet);
        return resultSet;
    }

    private void checkQuery(String query, Object ...props) {
        long count = query.chars().filter(ch -> ch == '?').count();
        int propsLength = props != null ? props.length : 0;
        if (count != propsLength) {
            throw new RuntimeException("Mismatch statement count");
        }

    }

}
