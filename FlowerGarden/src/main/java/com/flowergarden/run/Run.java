package com.flowergarden.run;

import com.flowergarden.FlowerGardenConfiguration;
import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.dao.impl.GeneralBouquetJdbcDao;
import com.flowergarden.dao.impl.GeneralFlowerJdbcDao;
import com.flowergarden.dao.impl.JdbcHandler;
import com.flowergarden.dao.json.GeneralBouquetJsonDao;
import com.flowergarden.domain.Customer;
import com.flowergarden.domain.bouquet.MarriedBouquet;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.OutputStreamWriter;
import java.io.Writer;


public class Run {

    public static void main(String[] args) throws JAXBException {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(FlowerGardenConfiguration.class);

        JdbcHandler jdbcHandler = context.getBean(JdbcHandler.class);

        GeneralFlowerDao flowerDao = context.getBean(GeneralFlowerJdbcDao.class);
        GeneralBouquetDao bouquetDao = context.getBean(GeneralBouquetJdbcDao.class);

        System.out.println(flowerDao.findAll());


        final GeneralBouquetJsonDao bouquetJsonDao = context.getBean("bouquetJsonDao",
                GeneralBouquetJsonDao.class);

        bouquetJsonDao.save((MarriedBouquet) bouquetDao.findOne(1), new OutputStreamWriter(System.out));


    }

}
