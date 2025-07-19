package com.davidelatina.bankingdemo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Singleton managing one single connection, and making it available through getDbConnection().
 * Does not return its instance.
 */
public class ConnectionManager {

  static private ConnectionManager instance;
  static private Connection dbConnection;

  public static Connection getDbConnection() {
    return dbConnection;
  }

  private ConnectionManager() {}

  /**
   * Create database connection if not already present, otherwise do nothing.
   * @throws SQLException
   */
  public static void init() {

    // Only allow one connection
    if (instance != null) {
      return;
    }

    // Load environment variables
    Dotenv dotenv = Dotenv.load();

    // Attempt to establish a db connection
    try {
      dbConnection = DriverManager.getConnection(
          dotenv.get("DB_URL"), 
          dotenv.get("DB_USER"), 
          dotenv.get("DB_PASSWORD"));
    }

    // Connection failed
    catch (SQLException e) {
      e.printStackTrace();
    }

    new ConnectionManager();

  }
}
