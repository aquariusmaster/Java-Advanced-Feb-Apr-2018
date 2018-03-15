package com.flowergarden.run;

import com.flowergarden.dao.GeneralFlowerDao;
import com.flowergarden.dao.impl.GeneralFlowerJdbcDao;
import com.flowergarden.dao.impl.JdbcHandler;
import com.flowergarden.domain.flowers.GeneralFlower;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class Run {

	public static void main(String[] args) throws IOException {

		File file = new File("flowergarden.db");
		String url = "jdbc:sqlite:"+file.getCanonicalFile().toURI();
		System.out.println(url);
		try(Connection conn = DriverManager.getConnection(url)) {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from flower");
			while (rs.next()) {
				System.out.println(rs.getString(2));
			}
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		JdbcHandler jdbcHandler = new JdbcHandler("");
		GeneralFlowerDao flowerDao = new GeneralFlowerJdbcDao(jdbcHandler);

		List<GeneralFlower> flowers = flowerDao.findAll();
        System.out.println(flowers);

	}

}
