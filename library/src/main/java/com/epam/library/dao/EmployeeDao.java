package com.epam.library.dao;

import com.epam.library.dao.exception.DaoException;
import com.epam.library.domain.Employee;

public interface EmployeeDao {
	void insert(Employee employee) throws DaoException;

	void delete(Integer employeeId) throws DaoException;

	Employee getEmployee(Integer employeeId) throws DaoException;

	void update(Employee employee) throws DaoException;
}
