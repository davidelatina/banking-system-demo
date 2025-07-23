package com.davidelatina.bankingdemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.math.BigInteger;

import com.davidelatina.bankingdemo.dao.util.ConnectionManager;
import com.davidelatina.bankingdemo.model.entity.Customer;

/**
 * Data-access object for {@link Customer}
 */
public class CustomerDAO {

  private ConnectionManager connectionManager;

  public CustomerDAO(ConnectionManager connectionManager) {
    this.connectionManager = connectionManager;
  }

  // --- Methods
  /**
   * Checks whether a given username is available in the customer database.
   *
   * @param username the username to check for availability
   * @return {@code true} if the username is not taken, {@code false} otherwise
   * @throws SQLException if a database access error occurs
   */
  public boolean checkUsernameAvailable(String username) throws SQLException {
    String query = "SELECT username FROM customer WHERE username = ?";

    Connection conn = connectionManager.getDbConnection();

    try (PreparedStatement stmt = conn.prepareStatement(query)) {

      stmt.setString(1, username);

      ResultSet rs = stmt.executeQuery();

      // Position cursor on first (and only) row. return true if no data
      return !rs.next();
    }
  }

  /**
   * Register a new customer to the database.
   */
  public void createCustomer(
      String username, String firstName, String lastName, int age, byte[] salt, byte[] hashedPassword)
      throws SQLException {

    String sql = "INSERT INTO customer (username, first_name, last_name, age, salt, hashed_password, registered_at)  VALUES (?, ?, ?, ?, ?, ?, NOW());";

    Connection conn = connectionManager.getDbConnection();

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, username);
      stmt.setString(2, firstName);
      stmt.setString(3, lastName);
      stmt.setInt(4, age);
      stmt.setBytes(5, salt);
      stmt.setBytes(6, hashedPassword);

      stmt.executeUpdate();
    }
  }

  /**
   * Retrieves a single customer from the database using their unique identifier.
   * <p>
   * If the user exists, constructs a new Customer record from result set data.
   * The type correspondences between Java and MySQL can be consulted at:
   * https://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
   * 
   * @param id unique identifier for the customer (must be positive and not null)
   * @return {@link Optional} containing the {@link Customer} if found,
   *         or {@link Optional#empty()} if no customer exists with the given id
   * @throws SQLException             if a database access error occurs or the
   *                                  connection is closed
   * @throws IllegalArgumentException if id is null or not positive
   * @see Customer
   * @see ConnectionManager
   */
  public Optional<Customer> get(BigInteger id) throws IllegalArgumentException, SQLException {
    // --- Parameter validation
    if (id == null) {
      throw new IllegalArgumentException("Customer ID cannot be null");
    }

    if (id.compareTo(BigInteger.valueOf(0)) <= 0) {
      throw new IllegalArgumentException("Customer ID must be a positive whole number");
    }

    // Prepared statement
    final String query = "SELECT id, username, first_name, last_name, age, registered_at FROM customer WHERE id = ?";

    Connection conn = connectionManager.getDbConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

      // Java BigInteger maps to MySQL BIGINT UNSIGNED
      pstmt.setObject(1, id);

      // Execute statement
      try (ResultSet rs = pstmt.executeQuery()) {

        // Position cursor on first (and only) row
        // Return empty optional if no data is available
        if (!rs.next()) {
          return Optional.empty();
        }

        // Extract data from result set
        Customer customer = new Customer(
            rs.getObject("id", BigInteger.class), // BIGINT UNSIGNED (SERIAL)
            rs.getString("username"), // VARCHAR
            rs.getString("first_name"), // VARCHAR
            rs.getString("last_name"), // VARCHAR
            rs.getInt("age"), // INT
            rs.getObject("registered_at", LocalDateTime.class) // DATETIME
        );

        return Optional.of(customer);
      }
    }
  }

  public byte[] getSalt(BigInteger id) throws IllegalArgumentException, SQLException {

    // --- Parameter validation
    if (id == null) {
      throw new IllegalArgumentException("Customer ID cannot be null");
    }

    if (id.compareTo(BigInteger.valueOf(0)) <= 0) {
      throw new IllegalArgumentException("Customer ID must be a positive whole number");
    }

    // Prepared statement
    final String query = "SELECT salt FROM customer WHERE id = ?";

    Connection conn = connectionManager.getDbConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

      // Java BigInteger maps to MySQL BIGINT UNSIGNED
      pstmt.setObject(1, id);

      // Execute statement
      try (ResultSet rs = pstmt.executeQuery()) {

        // Position cursor on first (and only) row
        // Throw exception if there is no data
        if (!rs.next()) {
          throw new IllegalArgumentException("No user by this id.");
        }

        // Extract data from result set
        return rs.getBytes("salt");
      }
    }
  }

  public byte[] getSalt(String username) throws IllegalArgumentException, SQLException {

    // --- Parameter validation
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null.");
    }

    if (username.isBlank()) {
      throw new IllegalArgumentException("Username cannot be blank.");
    }

    // Prepared statement
    final String query = "SELECT salt FROM customer WHERE username = ?";

    Connection conn = connectionManager.getDbConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, username);

      // Execute statement
      try (ResultSet rs = pstmt.executeQuery()) {

        // Position cursor on first (and only) row
        // Throw exception if there is no data
        if (!rs.next()) {
          throw new IllegalArgumentException("Unknown username.");
        }

        // Extract data from result set
        return rs.getBytes("salt");
      }
    }
  }

  public BigInteger getIdFromUsername(String username) throws IllegalArgumentException, SQLException {

    // --- Parameter validation
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null.");
    }

    if (username.isBlank()) {
      throw new IllegalArgumentException("Username cannot be blank.");
    }

    // Prepared statement
    final String query = "SELECT id FROM customer WHERE username = ?";

    Connection conn = connectionManager.getDbConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

      pstmt.setString(1, username);

      // Execute statement
      try (ResultSet rs = pstmt.executeQuery()) {

        // Position cursor on first (and only) row
        // Throw exception if there is no data
        if (!rs.next()) {
          throw new IllegalArgumentException("Unknown username.");
        }

        // Extract data from result set
        return rs.getObject("id", BigInteger.class);
      }
    }
  }

  public byte[] getHashedPassword(String username) throws IllegalArgumentException, SQLException {

    // --- Parameter validation
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null.");
    }

    if (username.isBlank()) {
      throw new IllegalArgumentException("Username cannot be blank.");
    }

    // Prepared statement
    final String query = "SELECT hashed_password FROM customer WHERE username = ?";

    Connection conn = connectionManager.getDbConnection();

    try (PreparedStatement pstmt = conn.prepareStatement(query)) {

      // Java BigInteger maps to MySQL BIGINT UNSIGNED
      pstmt.setString(1, username);

      // Execute statement
      try (ResultSet rs = pstmt.executeQuery()) {

        // Position cursor on first (and only) row
        // Throw exception if there is no data
        if (!rs.next()) {
          throw new IllegalArgumentException("Unknown username.");
        }

        // Extract data from result set
        return rs.getBytes("hashed_password");
      }
    }
  }



  public List<Customer> getAll() throws SQLException {
    String query = "SELECT id, username, first_name, last_name, age, registered_at FROM customer";
    ArrayList<Customer> customerList = new ArrayList<>();

    Connection conn = connectionManager.getDbConnection();

    try (
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      while (rs.next()) {
        customerList.add(new Customer(
            rs.getObject("id", BigInteger.class), // BIGINT UNSIGNED (SERIAL)
            rs.getString("username"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getInt("age"),
            rs.getObject("registered_at", LocalDateTime.class))); // DATETIME
      }
    }
    return customerList;
  }

  public void update(Customer user, String[] params) {

  }

}
