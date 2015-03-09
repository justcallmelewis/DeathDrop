package net.alextwelshie.minedrop.ranks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class ConnectionPoolManager {

	String						databaseUrl		= "jdbc:mysql://localhost:3306/survivalmc?autoReconnect=true";
	String						userName		= "db_user19910526";
	String						password		= "44ap9cDj@LR6?QEM74HWYUt@T";

	static Vector<Connection>	connectionPool	= new Vector<Connection>();

	public ConnectionPoolManager() {
		initialize();
	}

	public ConnectionPoolManager(String databaseUrl, String userName, String password) {
		this.databaseUrl = databaseUrl;
		this.userName = userName;
		this.password = password;
		initialize();
	}

	private void initialize() {
		initializeConnectionPool();
	}

	private void initializeConnectionPool() {
		while (!checkIfConnectionPoolIsFull()) {
			connectionPool.addElement(createNewConnectionForPool());
		}
	}

	private synchronized boolean checkIfConnectionPoolIsFull() {
		final int MAX_POOL_SIZE = 3;

		if (connectionPool.size() < MAX_POOL_SIZE) {
			return false;
		}

		return true;
	}

	//Creating a connection
	private Connection createNewConnectionForPool() {
		Connection connection = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(databaseUrl, userName, password);
		} catch (SQLException sqle) {
			System.err.println("SQLException: " + sqle);
			return null;
		} catch (ClassNotFoundException cnfe) {
			System.err.println("ClassNotFoundException: " + cnfe);
			return null;
		}

		return connection;
	}

	public synchronized Connection getConnectionFromPool() {

		if (!connectionPool.isEmpty()) {
			Connection existingConnection = connectionPool.lastElement();
			int lastIndex = connectionPool.size() - 1;
			connectionPool.removeElementAt(lastIndex);

			try {
				if (existingConnection.isClosed()) {
					notifyAll();
					return (getConnectionFromPool());
				} else {
					connectionPool.addElement(existingConnection);
					return (existingConnection);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public synchronized void returnConnectionToPool(Connection connection) {
		connectionPool.addElement(connection);
	}

}
