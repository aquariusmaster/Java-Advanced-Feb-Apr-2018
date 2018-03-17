package com.flowergarden.service;

import com.flowergarden.domain.flowers.GeneralFlower;

import java.util.List;

public interface GeneralFlowerService {

    GeneralFlower findOne(Integer flowerId);

    List<GeneralFlower> findAllByBouquetAndLengthIn(int bouquetId, int min, int max);
}
