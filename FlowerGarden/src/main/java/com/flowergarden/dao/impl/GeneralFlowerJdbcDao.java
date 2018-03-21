package com.flowergarden.dao.impl;

import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.dao.impl.sql_queries.SqlQueries;
import com.flowergarden.domain.flowers.Chamomile;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.domain.flowers.Rose;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository("flowerDao")
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

        final String sql = SqlQueries.SELECT_FLOWER_JOIN_BOUQUET + " WHERE flower_id=?";
        ResultSet rs = jdbcHandler.executeSelect(sql, flowerId);
        List<GeneralFlower> resultList = new GeneralFlowerExtractor().extract(rs);
        if (resultList.isEmpty()) return null;
        if (resultList.size() > 1) {
            throw new RuntimeException("Not unique query result");
        }
        return resultList.get(0);
    }

    @Override
    public List<GeneralFlower> findAll() {

        final String sql = SqlQueries.SELECT_FLOWER_JOIN_BOUQUET;
        ResultSet rs = jdbcHandler.executeSelect(sql);
        return new GeneralFlowerExtractor().extract(rs);

    }

    @Override
    public void delete(Integer flowerId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<GeneralFlower> findAllByBouquetId(Integer bouquetId) {
        final String sql = SqlQueries.SELECT_FLOWER_JOIN_BOUQUET +" WHERE bouquet_id=?";
        ResultSet rs = jdbcHandler.executeSelect(sql, bouquetId);
        return new GeneralFlowerExtractor().extract(rs);
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

}
