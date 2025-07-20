package com.davidelatina.bankingdemo.dao.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Singleton managing one single connection
 */
public enum ConnectionManager {

  INSTANCE;

  private Connection dbConnection;

  public Connection getDbConnection() {
    return this.dbConnection;
  }

  private ConnectionManager() {

    // Load environment variables
    Dotenv dotenv = Dotenv.load();

    // Attempt to establish a db connection
    try {
      this.dbConnection = DriverManager.getConnection(
          dotenv.get("DB_URL"),
          dotenv.get("DB_USER"),
          dotenv.get("DB_PASSWORD"));
    }

    // Connection failed
    catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
