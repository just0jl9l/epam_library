package com.epam.library.dao.connection_pool;

import java.sql.Connection;

import com.epam.library.dao.connection_pool.exception.ConnectionPoolException;

public interface ConnectionPool {

	void init() throws ConnectionPoolException;
	Connection getConnection() throws ConnectionPoolException;
	void close() throws ConnectionPoolException;

}
