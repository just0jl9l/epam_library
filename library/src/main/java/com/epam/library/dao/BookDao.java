package com.epam.library.dao;

import com.epam.library.dao.exception.DaoException;
import com.epam.library.domain.Book;

public interface BookDao {
	void insert(Book book) throws DaoException;

	void delete(Integer bookId) throws DaoException;

	Book getBook(Integer bookId) throws DaoException;

	void update(Book book) throws DaoException;
}
