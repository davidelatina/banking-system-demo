package com.davidelatina.bankingdemo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import java.math.BigInteger;

import com.davidelatina.bankingdemo.dao.util.ConnectionManager;
import com.davidelatina.bankingdemo.model.entity.Customer;
import com.davidelatina.bankingdemo.view.impl.MenuView;

/**
 * Data-access object for {@link Customer}
 */
public enum CustomerDAO {

  INSTANCE;

  /**
   * Register a new customer to the database.
   */
  public void createCustomer(
      String username, String firstName, String lastName, int age, byte[] salt, byte[] hashedPassword)
      throws SQLException {

    String sql = "INSERT INTO customer (username, first_name, last_name, age, salt, hashed_password, registered_at)  VALUES (?, ?, ?, ?, ?, ?, NOW());";

    Connection conn = ConnectionManager.INSTANCE.getDbConnection();

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, username);
      stmt.setString(2, firstName);
      stmt.setString(3, lastName);
      stmt.setInt(4, age);
      stmt.setBytes(5, salt);
      stmt.setBytes(6, hashedPassword);

      stmt.executeUpdate();
      MenuView.INSTANCE.displayMessage("User added to database.");

    } catch (SQLException ex) {
      throw ex;
    }
  }

  /**
   * Obtain a single customer from its id, if it exists.
   * 
   * @param id unique identifier for the user
   * @return {@link Optional} of {@link Customer}. Empty if user was not found.
   */
  public Optional<Customer> get(BigInteger id) throws SQLException {

    String query = "SELECT id, username, first_name, last_name, age, registered_at FROM customer WHERE id = " + id;

    Connection conn = ConnectionManager.INSTANCE.getDbConnection();

    try (
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

      // Position cursor on first (and only) row. empty return if no data
      if (!rs.next()) {
        return Optional.empty();
      }

      // There is data

      // The type correspondences between Java and MySQL
      // can be consulted in this table
      // https://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
      return Optional.of(new Customer(
          rs.getObject("id", BigInteger.class), // BIGINT UNSIGNED (SERIAL)
          rs.getString("username"),
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getInt("age"),
          rs.getObject("registered_at", LocalDateTime.class))); // DATETIME

      // An error occurred
    } catch (SQLException ex) {
      throw ex;
    }
  }

  public ArrayList<Customer> getAll() throws SQLException {
    String query = "SELECT id, username, first_name, last_name, age, registered_at FROM customer";
    ArrayList<Customer> customerList = new ArrayList<>();

    Connection conn = ConnectionManager.INSTANCE.getDbConnection();

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

      // An error occurred
    } catch (SQLException ex) {
      throw ex;
    }

    return customerList;
  }

  public void save(Customer customer) {

  }

  public void update(Customer user, String[] params) {

  }

}
