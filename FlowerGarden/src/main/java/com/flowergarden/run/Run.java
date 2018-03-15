package com.flowergarden.run;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.dao.impl.GeneralBouquetJdbcDao;
import com.flowergarden.dao.impl.GeneralFlowerJdbcDao;
import com.flowergarden.dao.impl.JdbcHandler;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.service.GeneralBouquetService;
import com.flowergarden.service.impl.GeneralBouquetServiceImpl;


public class Run {

    public static void main(String[] args) {

        JdbcHandler jdbcHandler = new JdbcHandler("flowergarden.db");

        GeneralFlowerDao flowerDao = new GeneralFlowerJdbcDao(jdbcHandler);
        GeneralBouquetDao bouquetDao = new GeneralBouquetJdbcDao(jdbcHandler);
        GeneralBouquetService bouquetService = new GeneralBouquetServiceImpl(bouquetDao, flowerDao);

        GeneralBouquet bouquet = bouquetService.findOne(1);
        System.out.println(bouquet.getPrice());

        System.out.println(bouquet.getFlowers());
        bouquet.sortByFreshness();
        System.out.println(bouquet.getFlowers());

    }

}
