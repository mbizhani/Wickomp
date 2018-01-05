package org.devocative.wickomp.demo.service;

import org.devocative.adroit.sql.InitDB;
import org.devocative.adroit.sql.NamedParameterStatement;
import org.devocative.adroit.sql.plugin.PaginationPlugin;
import org.devocative.adroit.sql.result.EColumnNameCase;
import org.devocative.adroit.sql.result.QueryVO;
import org.devocative.adroit.sql.result.ResultSetProcessor;
import org.devocative.adroit.sql.result.RowVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DbService {
	private static Connection connection;

	public static void init() {
		InitDB initDB = new InitDB();
		initDB
			.setDriver("org.hsqldb.jdbcDriver")
			.setUrl("jdbc:hsqldb:mem:wickomp")
			.setUsername("wickomp")
			.setPassword("")
			.addScript("src/test/resources/init_hsql.sql");

		initDB.build();

		connection = initDB.getConnection();
	}

	public static void shutdown() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<RowVO> execute(String sql, long pageIndex, long pageSize) {
		NamedParameterStatement nps = new NamedParameterStatement(connection, sql);
		nps.addPlugin(new PaginationPlugin((pageIndex - 1) * pageSize + 1, pageSize, PaginationPlugin.findDatabaseType(connection)));
		QueryVO process;
		try {
			ResultSet rs = nps.executeQuery();
			process = ResultSetProcessor.process(rs, EColumnNameCase.LOWER);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return process.toListOfMap();
	}
}
