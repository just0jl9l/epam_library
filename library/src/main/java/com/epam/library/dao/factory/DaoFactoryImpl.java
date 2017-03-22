package com.epam.library.dao.factory;

import com.epam.library.dao.BookAndEmployeeDao;
import com.epam.library.dao.BookDao;
import com.epam.library.dao.EmployeeDao;
import com.epam.library.dao.impl.BookAndEmployeeDaoImpl;
import com.epam.library.dao.impl.BookDaoImpl;
import com.epam.library.dao.impl.EmployeeDaoImpl;

public class DaoFactoryImpl implements DaoFactory {

	private static DaoFactoryImpl factory = new DaoFactoryImpl();

	private BookDao bookDao = new BookDaoImpl();
	private EmployeeDao employeeDao = new EmployeeDaoImpl();
	private BookAndEmployeeDao bookAndEmployeeDao = new BookAndEmployeeDaoImpl();

	private DaoFactoryImpl() {
	}

	public static DaoFactoryImpl getInstance() {
		return factory;
	}

	@Override
	public BookDao getBookDao() {
		return bookDao;
	}

	@Override
	public EmployeeDao getEmployeeDao() {
		return employeeDao;
	}

	@Override
	public BookAndEmployeeDao getBookAndEmployeeDao() {
		return bookAndEmployeeDao;
	}



}
