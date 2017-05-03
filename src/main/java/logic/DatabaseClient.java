package logic;

import java.sql.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseClient {
	private static final Logger logger = LoggerFactory.getLogger(DatabaseClient.class);
	private static final DatabaseClient dbClient = new DatabaseClient();
	private Connection connect = null;

	String defaultDatabase = "searches";
	String dbUser = "root";
	String dbPwd = "gomysql!";
	String server = "localhost";
	
	public static DatabaseClient getInstance() {
		return dbClient;
	}

	private DatabaseClient() {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection(
					"jdbc:mysql://" + server + "/" + defaultDatabase + "?user=" + dbUser + "&password=" + dbPwd);
			connect.setAutoCommit(true);
			connect.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			logger.info("Initalized db successful");
		} catch (ClassNotFoundException | SQLException e) {
			logger.info("Could not initialized db: " + e.getMessage());
		}
	}
}
