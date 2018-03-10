package com.flowergarden.dao.impl;

import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.flowers.Chamomile;
import com.flowergarden.flowers.GeneralFlower;
import com.flowergarden.flowers.Rose;
import com.flowergarden.flowers.Tulip;
import com.flowergarden.properties.FreshnessInteger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GeneralFlowerDaoJdbcImpl implements GeneralFlowerDao {

    @Override
    public GeneralFlower saveOrUpdate(GeneralFlower flower) {
        return null;
    }

    @Override
    public GeneralFlower findOne(Integer flowerId) {

        Connection conn = null;
        Statement stmt = null;
        GeneralFlower flower = null;
        try {
            conn = DBUtils.getConnection();
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM flower WHERE id=" + flowerId + ";");
            List<GeneralFlower> resultList = this.extractFlowersFromResultSet(rs);
            if (resultList.size() > 1) {
                throw new RuntimeException("Not unique query result");
            }
            flower = resultList.get(0);
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

        return flower;
    }

    @Override
    public List<GeneralFlower> findAll() {

        final String queryAll = "SELECT * FROM flower;";
        Connection conn = DBUtils.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        List<GeneralFlower> flowers;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryAll);
            flowers = extractFlowersFromResultSet(rs);
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

        return flowers;
    }

    @Override
    public void delete(Integer flowerId) {
        throw new UnsupportedOperationException();
    }

    private List<GeneralFlower> extractFlowersFromResultSet(ResultSet rs) throws SQLException {

        List<GeneralFlower> flowers = new ArrayList<>();

        while (rs.next()) {
            GeneralFlower flower;

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

            flowers.add(null);

        }

        return flowers;
    }

}
