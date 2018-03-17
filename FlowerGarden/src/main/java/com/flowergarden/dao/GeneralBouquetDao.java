package com.flowergarden.dao;

import com.flowergarden.domain.bouquet.GeneralBouquet;

import java.util.List;

public interface GeneralBouquetDao {

    void saveOrUpdate(GeneralBouquet bouquet);
    GeneralBouquet findOne(Integer bouquetId);
    List<GeneralBouquet> findAll();
    void delete(Integer bouquetId);
}
