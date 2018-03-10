package com.flowergarden.dao;

import com.flowergarden.flowers.Flower;
import com.flowergarden.flowers.GeneralFlower;

import java.util.List;

public interface GeneralFlowerDao {

    void saveOrUpdate(GeneralFlower flower);
    GeneralFlower findOne(Integer flowerId);
    List<GeneralFlower> findAll();
    void delete(Integer flowerId);
}
