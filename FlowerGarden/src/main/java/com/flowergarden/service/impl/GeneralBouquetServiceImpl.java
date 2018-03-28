package com.flowergarden.service.impl;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.service.GeneralBouquetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralBouquetServiceImpl implements GeneralBouquetService {

    private final GeneralBouquetDao bouquetDao;

    @Autowired
    public GeneralBouquetServiceImpl(GeneralBouquetDao bouquetDao) {
        this.bouquetDao = bouquetDao;
    }

    @Override
    public GeneralBouquet findOne(Integer bouquetId) {
        return bouquetDao.findOne(bouquetId);
    }

}
