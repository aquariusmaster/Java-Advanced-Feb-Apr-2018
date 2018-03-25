package com.flowergarden.dao.impl;

import com.flowergarden.annotations.WorkMeter;
import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.impl.sql_queries.SqlQueries;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GeneralBouquetJdbcDao implements GeneralBouquetDao {

    private final JdbcHandler jdbcHandler;
    private final GeneralBouquetExtractor bouquetExtractor;

    @Autowired
    public GeneralBouquetJdbcDao(JdbcHandler jdbcHandler, GeneralBouquetExtractor bouquetExtractor) {
        this.jdbcHandler = jdbcHandler;
        this.bouquetExtractor = bouquetExtractor;
    }

    @Override
    public void saveOrUpdate(GeneralBouquet bouquet) {

        if (bouquet.getId() != null) {
            update(bouquet);
        } else {
            create(bouquet);
        }

    }

    @WorkMeter
    @Override
    public GeneralBouquet findOne(Integer bouquetId) {
        final String sql = SqlQueries.SELECT_BOUQUET_JOIN_FLOWER + " WHERE bouquet_id=?";
        List<GeneralBouquet> bouquets = bouquetExtractor.extract(jdbcHandler
                .executeSelect(sql, bouquetId));
        if (bouquets == null) {
            return null;
        }
        if (bouquets.size() > 1) {
            throw new RuntimeException("Not unique query result");
        }
        return bouquets.get(0);
    }

    @Override
    public List<GeneralBouquet> findAll() {
        final String sql = SqlQueries.SELECT_BOUQUET_JOIN_FLOWER;

        return bouquetExtractor.extract(jdbcHandler.executeSelect(sql));
    }

    @Override
    public void delete(Integer bouquetId) {
        throw new UnsupportedOperationException();
    }

    private int create(GeneralBouquet bouquet) {
        final String sql =
                "INSERT INTO bouquet (assemble_price, name) VALUES (?,?)";

        return jdbcHandler.executeUpdate(sql,
                bouquet.getAssemblePrice(),
                bouquet.getClass().getSimpleName().toLowerCase());
    }

    private int update(GeneralBouquet bouquet) {
        final String sql =
                "UPDATE bouquet SET assemble_price=? WHERE id=?";

        return jdbcHandler.executeUpdate(sql,
                bouquet.getAssemblePrice(),
                bouquet.getId());
    }
}
