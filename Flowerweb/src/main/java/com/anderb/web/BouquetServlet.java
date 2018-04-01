package com.anderb.web;

import com.flowergarden.domain.bouquet.GeneralBouquet;
import com.flowergarden.domain.flowers.GeneralFlower;
import com.flowergarden.service.GeneralBouquetService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class BouquetServlet extends HttpServlet {
    private static Logger log = Logger.getLogger(BouquetServlet.class);

    @Autowired
    private GeneralBouquetService bouquetService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
        super.init(config);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        String pathInfo = request.getPathInfo();
        int id = Integer.parseInt(pathInfo.split("/")[1]);
        GeneralBouquet bouquet = bouquetService.findOne(id);

        PrintWriter out = response.getWriter();
        out.println("<h2>Bouquet " + id + ": assembled price= " + bouquet.getAssemblePrice() + "</h2>");
        out.println("<h2>Bouquet cost: " + bouquet.getPrice() + "</h2>");
        out.println("<h2>Flowers: </h2>");
        for (GeneralFlower flower : bouquet.getFlowers()) {
            out.println("<h3> " + flower + "</h3>");
        }
        out.close();
    }
}
