package com.flowergarden.service.impl;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.service.GeneralBouquetService;

import java.util.List;

public class GeneralBouquetServiceImpl implements GeneralBouquetService {

    private GeneralBouquetDao bouquetDao;

    private GeneralFlowerDao flowerDao;

    public GeneralBouquetServiceImpl(GeneralBouquetDao bouquetDao, GeneralFlowerDao flowerDao) {
        this.bouquetDao = bouquetDao;
        this.flowerDao = flowerDao;
    }

    @Override
    public GeneralBouquet findOne(Integer bouquetId) {
        GeneralBouquet bouquet = bouquetDao.findOne(bouquetId);
        if (bouquet != null) {
            List<GeneralFlower> flowers = flowerDao.findAllByBouquetId(bouquetId);
            flowers.stream().forEach(e -> e.setBouquet(bouquet));
            bouquet.setFlowerList(flowers);
        }
        return bouquet;
    }
}
