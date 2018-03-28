package com.flowergarden.service.impl;

import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.service.GeneralFlowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneralFlowerServiceImpl implements GeneralFlowerService {

    private final GeneralFlowerDao flowerDao;

    @Autowired
    public GeneralFlowerServiceImpl(GeneralFlowerDao flowerDao) {
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
