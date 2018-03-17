package com.flowergarden.run;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.dao.impl.GeneralBouquetJdbcDao;
import com.flowergarden.dao.impl.GeneralFlowerJdbcDao;
import com.flowergarden.dao.impl.JdbcHandler;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.service.GeneralBouquetService;
import com.flowergarden.service.GeneralFlowerService;
import com.flowergarden.service.impl.GeneralBouquetServiceImpl;
import com.flowergarden.service.impl.GeneralFlowerServiceImpl;

import java.util.List;


public class Run {

    public static void main(String[] args) {

        JdbcHandler jdbcHandler = new JdbcHandler("flowergarden.db");

        GeneralFlowerDao flowerDao = new GeneralFlowerJdbcDao(jdbcHandler);
        GeneralBouquetDao bouquetDao = new GeneralBouquetJdbcDao(jdbcHandler);
        GeneralBouquetService bouquetService = new GeneralBouquetServiceImpl(bouquetDao, flowerDao);
        GeneralFlowerService flowerService = new GeneralFlowerServiceImpl(bouquetDao, flowerDao);

        GeneralBouquet bouquet = bouquetService.findOne(1);
        System.out.println(bouquet);
        System.out.println(bouquet.getFlowers());
        System.out.println(bouquet.getPrice());


        System.out.println(bouquet.getFlowers());
        bouquet.sortByFreshness();
        System.out.println(bouquet.getFlowers());

        List<GeneralFlower> flowers = flowerService.findAllByBouquetAndLengthIn(1, 10, 12);
        System.out.println(flowers);

        GeneralFlower flower = flowerService.findOne(7);
        System.out.println(flower);

        System.out.println(flowerDao.findAll());

    }

}
