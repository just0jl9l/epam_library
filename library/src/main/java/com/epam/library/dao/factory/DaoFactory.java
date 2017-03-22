package com.epam.library.dao.factory;

import com.epam.library.dao.BookAndEmployeeDao;
import com.epam.library.dao.BookDao;
import com.epam.library.dao.EmployeeDao;

public interface DaoFactory {

	BookDao getBookDao();
	EmployeeDao getEmployeeDao();
	BookAndEmployeeDao getBookAndEmployeeDao();

}
