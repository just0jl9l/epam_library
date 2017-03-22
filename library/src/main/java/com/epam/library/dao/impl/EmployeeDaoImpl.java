package com.epam.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.epam.library.dao.EmployeeDao;
import com.epam.library.dao.connection_pool.exception.ConnectionPoolException;
import com.epam.library.dao.connection_pool.impl.ConnectionPoolImpl;
import com.epam.library.dao.exception.DaoException;
import com.epam.library.domain.Employee;

public class EmployeeDaoImpl implements EmployeeDao {
	
	public static final String SQL_INSERT_EMPLOYEE = "INSERT INTO employee(name, email, date_of_birth) VALUES(?, ?, ?);";
	public static final String SQL_DELETE_EMPLOYEE_BY_ID = "DELETE FROM employee WHERE id = ?;";
	public static final String SQL_GET_EMPLOYEE_BY_ID = "SELECT name, email, date_of_birth FROM employee WHERE id = ?;";
	public static final String SQL_UPDATE_EMPLOYEE = "UPDATE employee SET name = ?, email = ?, date_of_birth = ? WHERE id = ?;";
	
	
	@Override
	public void insert(Employee employee) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_INSERT_EMPLOYEE)) {
			stm.setString(1, employee.getName());
			stm.setString(2, employee.getEmail());
			stm.setTimestamp(3, employee.getDateOfBirth());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}

	}

	@Override
	public void delete(Integer employeeId) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_DELETE_EMPLOYEE_BY_ID)) {
			stm.setInt(1, employeeId);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}
	}

	@Override
	public Employee getEmployee(Integer employeeId) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_GET_EMPLOYEE_BY_ID)) {
			stm.setInt(1, employeeId);
			ResultSet rs = stm.executeQuery();
			Employee Employee = null;
			if (rs.next()) {
				Employee = new Employee();
				Employee.setId(employeeId);
				Employee.setName(rs.getString(1));
				Employee.setEmail(rs.getString(2));
				Employee.setDateOfBirth(rs.getTimestamp(3));
			}
			return Employee;
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}
	}

	@Override
	public void update(Employee employee) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_UPDATE_EMPLOYEE)) {
			stm.setString(1, employee.getName());
			stm.setString(2, employee.getEmail());
			stm.setTimestamp(3, employee.getDateOfBirth());
			stm.setInt(4, employee.getId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}
		
	}
}
