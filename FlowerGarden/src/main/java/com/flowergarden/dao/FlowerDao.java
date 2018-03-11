package com.flowergarden.dao;

import com.flowergarden.domain.flowers.GeneralFlower;
import java.util.List;

public interface FlowerDao {

    void saveOrUpdate(GeneralFlower flower);
    GeneralFlower findOne(Integer flowerId);
    List<GeneralFlower> findAll();
    void delete(Integer flowerId);
}
