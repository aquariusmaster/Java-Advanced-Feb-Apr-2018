package com.flowergarden.service.impl;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.service.GeneralFlowerService;

import java.util.List;
import java.util.stream.Collectors;

public class GeneralFlowerServiceImpl implements GeneralFlowerService {

    private final GeneralBouquetDao bouquetDao;

    private final GeneralFlowerDao flowerDao;

    public GeneralFlowerServiceImpl(GeneralBouquetDao bouquetDao, GeneralFlowerDao flowerDao) {
        this.bouquetDao = bouquetDao;
        this.flowerDao = flowerDao;
    }

    @Override
    public GeneralFlower findOne(Integer flowerId) {
        return flowerDao.findOne(flowerId);
    }

    @Override
    public List<GeneralFlower> findAllByBouquetAndLengthIn(int bouquetId, int min, int max) {

        List<GeneralFlower> flowers = flowerDao.findAllByBouquetId(bouquetId);
        return flowers.stream().
                filter(e -> e.getLength() >= min && e.getLength() <= max).
                collect(Collectors.toList());
    }
}
