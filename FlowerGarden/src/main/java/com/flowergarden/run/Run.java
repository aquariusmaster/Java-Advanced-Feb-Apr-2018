package com.flowergarden.run;

import com.flowergarden.FlowerGardenConfiguration;
import com.flowergarden.dao.GeneralBouquetDao;
import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.dao.impl.GeneralBouquetJdbcDao;
import com.flowergarden.dao.impl.GeneralFlowerJdbcDao;
import com.flowergarden.dao.impl.JdbcHandler;
import com.flowergarden.dao.json.GeneralBouquetJsonDao;
import com.flowergarden.domain.bouquet.GeneralBouquet;
import org.codehaus.jettison.json.JSONException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;


public class Run {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(FlowerGardenConfiguration.class);

        JdbcHandler jdbcHandler = context.getBean(JdbcHandler.class);

        GeneralFlowerDao flowerDao = context.getBean(GeneralFlowerJdbcDao.class);
        GeneralBouquetDao bouquetDao = context.getBean(GeneralBouquetJdbcDao.class);

        System.out.println(flowerDao.findAll());


        final GeneralBouquetJsonDao bouquetJsonDao = context.getBean("bouquetJsonDao",
                GeneralBouquetJsonDao.class);

        StringWriter stringWriter = new StringWriter();

        bouquetJsonDao.writeJson(bouquetDao.findOne(1), stringWriter);
        System.out.println(stringWriter);

        GeneralBouquet bouquet = bouquetJsonDao.readJson(stringWriter.toString());
        System.out.println(bouquet);

    }

}
