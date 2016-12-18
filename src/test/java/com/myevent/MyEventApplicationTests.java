package com.myevent;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class MyEventApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Test
	public void contextLoads() {
	}

	@Test
	public void dbConnectTest() throws SQLException {
		System.out.println(dataSource);

		Connection connect = dataSource.getConnection();

		System.out.println(connect);

		connect.close();
	}

	@Test
	public void sqlSessionFactoryTest() {
		System.out.println(sqlSessionFactory);
	}
}
