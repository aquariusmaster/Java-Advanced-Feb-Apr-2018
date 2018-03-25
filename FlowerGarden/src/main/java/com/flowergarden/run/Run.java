package com.flowergarden.run;

import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.dao.impl.GeneralBouquetJdbcDao;
import com.flowergarden.dao.impl.GeneralFlowerJdbcDao;
import com.flowergarden.dao.impl.JdbcHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Run {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("com.flowergarden");

        JdbcHandler jdbcHandler = context.getBean(JdbcHandler.class);

        GeneralFlowerDao flowerDao = context.getBean(GeneralFlowerJdbcDao.class);
        GeneralBouquetDao bouquetDao = context.getBean(GeneralBouquetJdbcDao.class);

        System.out.println(flowerDao.findAll());


    }

}
