package com.epam.library.conteroller;

import java.sql.Timestamp;

import com.epam.library.dao.EmployeeDao;
import com.epam.library.dao.connection_pool.ConnectionPool;
import com.epam.library.dao.connection_pool.exception.ConnectionPoolException;
import com.epam.library.dao.connection_pool.impl.ConnectionPoolImpl;
import com.epam.library.dao.exception.DaoException;
import com.epam.library.dao.factory.DaoFactory;
import com.epam.library.dao.factory.DaoFactoryImpl;
import com.epam.library.domain.Employee;

public class Main {
	public static void main(String[] args) throws ConnectionPoolException, DaoException {
		ConnectionPool pool= ConnectionPoolImpl.getInstance();
		pool.init();
		DaoFactory factory = DaoFactoryImpl.getInstance();
		EmployeeDao dao = factory.getEmployeeDao();
		Employee b = new Employee();
		b.setId(13);
		b.setName("la");
		b.setEmail("lalal@la.la");
		b.setDateOfBirth(new Timestamp(122139));
	dao.delete(13);
		pool.close();
	}
}
