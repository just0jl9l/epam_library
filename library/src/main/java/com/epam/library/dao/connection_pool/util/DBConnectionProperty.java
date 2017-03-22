package com.epam.library.dao.connection_pool.util;

import java.util.ResourceBundle;

public class DBConnectionProperty {
	private static final DBConnectionProperty property = new DBConnectionProperty();
	private static ResourceBundle bundle = ResourceBundle.getBundle("db");

	private DBConnectionProperty() {}

	public static DBConnectionProperty getInstance() {
		return property;
	}

	public String getValue(String key) {
		return bundle.getString(key);
	}
}
