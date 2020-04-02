package org.natc.app;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest
@AutoConfigureEmbeddedDatabase
class NATCApplicationTests {

	@Autowired
	private DataSource dataSource;

	@Test
	void contextLoads() throws SQLException {

		final Statement statement = dataSource.getConnection().createStatement();

		statement.execute("select version()");

		final ResultSet resultSet = statement.getResultSet();

		while (resultSet.next()) {
			System.out.println(resultSet.getString("version"));
		}
	}
}
