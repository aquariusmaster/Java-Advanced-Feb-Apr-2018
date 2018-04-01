package com.anderb.web;

import com.flowergarden.service.GeneralBouquetService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        log.debug("");
        PrintWriter writer = resp.getWriter();
        writer.println("Bouquet 1: " + bouquetService.findOne(1));
        writer.close();
    }
}
