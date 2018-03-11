package com.flowergarden.dao;

import com.flowergarden.domain.bouquet.Bouquet;

import java.util.List;

public interface BouquetDao {

    void saveOrUpdate(Bouquet bouquet);
    Bouquet findOne(Integer bouquetId);
    List<Bouquet> findAll(Integer bouquetId);
    void delete(Integer bouquetId);
}
