package com.flowergarden.service.impl;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.service.GeneralBouquetService;

public class GeneralBouquetServiceImpl implements GeneralBouquetService {

    private GeneralBouquetDao bouquetDao;

    public GeneralBouquetServiceImpl(GeneralBouquetDao bouquetDao) {
        this.bouquetDao = bouquetDao;
    }

    @Override
    public GeneralBouquet findOne(Integer bouquetId) {
        return bouquetDao.findOne(bouquetId);
    }

}
