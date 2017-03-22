package com.epam.library.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.epam.library.dao.BookDao;
import com.epam.library.dao.connection_pool.exception.ConnectionPoolException;
import com.epam.library.dao.connection_pool.impl.ConnectionPoolImpl;
import com.epam.library.dao.exception.DaoException;
import com.epam.library.domain.Book;

public class BookDaoImpl implements BookDao {
	
	public static final String SQL_INSERT_BOOK = "INSERT INTO book(title, author, brief, publish_year) VALUES(?, ?, ?, ?);";
	public static final String SQL_DELETE_BOOK_BY_ID = "DELETE FROM book WHERE id = ?;";
	public static final String SQL_GET_BOOK_BY_ID = "SELECT title, author, brief, publish_year FROM book WHERE id = ?;";
	public static final String SQL_UPDATE_BOOK = "UPDATE book SET title = ?, author = ?, brief = ?, publish_year = ? WHERE id = ?;";
	
	
	@Override
	public void insert(Book book) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_INSERT_BOOK)) {
			stm.setString(1, book.getTitle());
			stm.setString(2, book.getAuthor());
			stm.setString(3, book.getBrief());
			stm.setInt(4, book.getYearOfPublishing());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}

	}

	@Override
	public void delete(Integer bookId) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_DELETE_BOOK_BY_ID)) {
			stm.setInt(1, bookId);
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}
	}

	@Override
	public Book getBook(Integer bookId) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_GET_BOOK_BY_ID)) {
			stm.setInt(1, bookId);
			ResultSet rs = stm.executeQuery();
			Book book = null;
			if (rs.next()) {
				book = new Book();
				book.setId(bookId);
				book.setTitle(rs.getString(1));
				book.setAuthor(rs.getString(2));
				book.setBrief(rs.getString(3));
				book.setYearOfPublishing(rs.getInt(4));
			}
			return book;
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}
	}

	@Override
	public void update(Book book) throws DaoException {
		try (Connection connection = ConnectionPoolImpl.getInstance().getConnection();
				PreparedStatement stm = connection.prepareStatement(SQL_UPDATE_BOOK)) {
			stm.setString(1, book.getTitle());
			stm.setString(2, book.getAuthor());
			stm.setString(3, book.getBrief());
			stm.setInt(4, book.getYearOfPublishing());
			stm.setInt(5, book.getId());
			stm.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException("SQLException", e);
		} catch (ConnectionPoolException e1) {
			throw new DaoException("ConnectionPoolException", e1);
		}
		
	}

}
