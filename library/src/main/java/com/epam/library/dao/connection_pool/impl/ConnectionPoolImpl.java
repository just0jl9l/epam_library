package com.epam.library.dao.connection_pool.impl;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.epam.library.dao.connection_pool.ConnectionPool;
import com.epam.library.dao.connection_pool.exception.ConnectionPoolException;
import com.epam.library.dao.connection_pool.util.ConnectionPoolPropertyConstant;
import com.epam.library.dao.connection_pool.util.DBConnectionProperty;



public class ConnectionPoolImpl implements ConnectionPool {

	private static ConnectionPoolImpl pool = new ConnectionPoolImpl();
	private static final Logger logger = LogManager.getLogger(Logger.class.getName());

	private ArrayBlockingQueue<Connection> freeConnections;
	private ArrayBlockingQueue<Connection> givenConnections;

	private String driver;
	private String url;
	private String user;
	private String password;
	private Integer numberOfConnections;

	private ConnectionPoolImpl() {
		DBConnectionProperty property = DBConnectionProperty.getInstance();
		this.driver = property.getValue(ConnectionPoolPropertyConstant.DRIVER);
		this.url = property.getValue(ConnectionPoolPropertyConstant.URL);
		this.password = property.getValue(ConnectionPoolPropertyConstant.PASSWORD);
		this.user = property.getValue(ConnectionPoolPropertyConstant.USER);
		try {
			this.numberOfConnections = Integer
					.parseInt(property.getValue(ConnectionPoolPropertyConstant.CONNECTIONS_NUMBER));
		} catch (NumberFormatException e) {
			numberOfConnections = 5;
		}
	}

	public static ConnectionPool getInstance() {
		return pool;
	}

	public void init() throws ConnectionPoolException {
		try {
			Class.forName(driver);
			freeConnections = new ArrayBlockingQueue<>(numberOfConnections);
			givenConnections = new ArrayBlockingQueue<>(numberOfConnections);
			for (Integer i = 0; i < numberOfConnections; i++) {
				freeConnections.add(new DBConnection(DriverManager.getConnection(url, user, password)));
			}
		} catch (SQLException e) {
			throw new ConnectionPoolException("SQLException", e);
		} catch (ClassNotFoundException e) {
			throw new ConnectionPoolException("ClassNotFoundException", e);
		}

		logger.info("ConnectionPool initialized");
	}

	public Connection getConnection() throws ConnectionPoolException {
		Connection connection = null;
		try {
			connection = freeConnections.take();
			givenConnections.add(connection);
		} catch (InterruptedException e) {
			throw new ConnectionPoolException("InterruptedException", e);
		}
		return connection;
	}

	public void close() throws ConnectionPoolException {
		try {
			close(freeConnections);
			close(givenConnections);
		} catch (SQLException e) {
			throw new ConnectionPoolException("SQLException ", e);
		} catch (InterruptedException e) {
			throw new ConnectionPoolException("InterruptedException ", e);
		}

		logger.info("ConnectionPool closed");
	}

	private void close(ArrayBlockingQueue<Connection> queue) throws SQLException, InterruptedException {
		DBConnection dbconnection;
		for (Connection con : queue) {
			dbconnection = (DBConnection) con;
			dbconnection.closeConnection();
			queue.remove(dbconnection);

		}
	}

	class DBConnection implements Connection {
		private Connection connection;

		public DBConnection(Connection connection) throws SQLException {
			this.connection = connection;
			connection.setAutoCommit(true);
		}

		public void closeConnection() throws SQLException {
			connection.close();
		}
		@Override
		public boolean isClosed() throws SQLException {
			return connection.isClosed();
		}
		@Override
		public void close() {
			try {
				connection.setAutoCommit(true);
				connection.setReadOnly(false);
				givenConnections.remove(this);
				freeConnections.put(this);
			} catch (InterruptedException e) {
				throw new RuntimeException("InterruptedException occurred during connection closing", e);
			} catch (SQLException e) {
				throw new RuntimeException("SQLException occurred during connection closing", e);
			}

		}
		@Override
		public boolean isWrapperFor(Class<?> iface) throws SQLException {
			return connection.isWrapperFor(iface);
		}
		@Override
		public <T> T unwrap(Class<T> iface) throws SQLException {
			return connection.unwrap(iface);
		}
		@Override
		public void abort(Executor executor) throws SQLException {
			connection.abort(executor);

		}
		@Override
		public void clearWarnings() throws SQLException {
			connection.clearWarnings();

		}
		@Override
		public void commit() throws SQLException {
			connection.commit();

		}
		@Override
		public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
			return connection.createArrayOf(typeName, elements);
		}
		@Override
		public Blob createBlob() throws SQLException {
			return connection.createBlob();
		}
		@Override
		public Clob createClob() throws SQLException {
			return connection.createClob();
		}
		@Override
		public NClob createNClob() throws SQLException {
			return connection.createNClob();
		}
		@Override
		public SQLXML createSQLXML() throws SQLException {

			return connection.createSQLXML();
		}

		@Override
		public Statement createStatement() throws SQLException {

			return connection.createStatement();
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {

			return connection.createStatement(resultSetType, resultSetConcurrency);
		}

		@Override
		public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
				throws SQLException {
			return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public Struct createStruct(String typeName, Object[] attributes) throws SQLException {

			return connection.createStruct(typeName, attributes);
		}

		@Override
		public boolean getAutoCommit() throws SQLException {
			return connection.getAutoCommit();
		}

		@Override
		public String getCatalog() throws SQLException {
			return connection.getCatalog();
		}

		@Override
		public Properties getClientInfo() throws SQLException {
			return connection.getClientInfo();
		}

		@Override
		public String getClientInfo(String name) throws SQLException {
			return connection.getClientInfo(name);
		}

		@Override
		public int getHoldability() throws SQLException {
			return connection.getHoldability();
		}

		@Override
		public DatabaseMetaData getMetaData() throws SQLException {
			return connection.getMetaData();
		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			return connection.getNetworkTimeout();
		}

		@Override
		public String getSchema() throws SQLException {
			return connection.getSchema();
		}

		@Override
		public int getTransactionIsolation() throws SQLException {
			return connection.getTransactionIsolation();
		}

		@Override
		public Map<String, Class<?>> getTypeMap() throws SQLException {
			return connection.getTypeMap();
		}

		@Override
		public SQLWarning getWarnings() throws SQLException {
			return connection.getWarnings();
		}

		@Override
		public boolean isReadOnly() throws SQLException {
			return connection.isReadOnly();
		}

		@Override
		public boolean isValid(int timeout) throws SQLException {
			return connection.isValid(timeout);
		}

		@Override
		public String nativeSQL(String sql) throws SQLException {
			return connection.nativeSQL(sql);
		}

		@Override
		public CallableStatement prepareCall(String sql) throws SQLException {
			return connection.prepareCall(sql);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public PreparedStatement prepareStatement(String sql) throws SQLException {

			return connection.prepareStatement(sql);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
			return connection.prepareStatement(sql, autoGeneratedKeys);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
			return connection.prepareStatement(sql, columnIndexes);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
			return connection.prepareStatement(sql, columnNames);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
				throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		@Override
		public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
				int resultSetHoldability) throws SQLException {
			return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
		}

		@Override
		public void releaseSavepoint(Savepoint savepoint) throws SQLException {
			connection.releaseSavepoint(savepoint);

		}

		@Override
		public void rollback() throws SQLException {
			connection.rollback();

		}

		@Override
		public void rollback(Savepoint savepoint) throws SQLException {
			connection.rollback(savepoint);

		}

		@Override
		public void setAutoCommit(boolean autoCommit) throws SQLException {
			connection.setAutoCommit(autoCommit);

		}

		@Override
		public void setCatalog(String catalog) throws SQLException {
			connection.setCatalog(catalog);

		}

		@Override
		public void setClientInfo(Properties properties) throws SQLClientInfoException {
			connection.setClientInfo(properties);

		}

		@Override
		public void setClientInfo(String name, String value) throws SQLClientInfoException {
			connection.setClientInfo(name, value);

		}

		@Override
		public void setHoldability(int holdability) throws SQLException {
			connection.setHoldability(holdability);

		}

		@Override
		public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
			connection.setNetworkTimeout(executor, milliseconds);

		}

		@Override
		public void setReadOnly(boolean readOnly) throws SQLException {
			connection.setReadOnly(readOnly);

		}

		@Override
		public Savepoint setSavepoint() throws SQLException {
			return connection.setSavepoint();
		}

		@Override
		public Savepoint setSavepoint(String name) throws SQLException {
			return connection.setSavepoint(name);
		}

		@Override
		public void setSchema(String schema) throws SQLException {
			connection.setSchema(schema);

		}

		@Override
		public void setTransactionIsolation(int level) throws SQLException {
			connection.setTransactionIsolation(level);

		}

		@Override
		public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
			connection.setTypeMap(map);

		}

	}

}
