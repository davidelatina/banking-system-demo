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

/**
 * Data-access object for @see Customer
 */
public enum CustomerDAO {

  INSTANCE;

  /**
   * Obtain a single customer from its id, if it exists.
   * 
   * @param id unique identifier for the user
   * @return {@link Optional} of {@link Customer}. Empty if user was not found.
   */
  public Optional<Customer> get(BigInteger id) {

    // id is equal to or lesser than zero
    if (id.compareTo(BigInteger.valueOf(0)) <= 0) {
      return Optional.empty();
    }

    String sql = "SELECT * FROM customer WHERE id = " + id;
    
    Connection conn = ConnectionManager.INSTANCE.getDbConnection();

    try (
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      // Position cursor on first (and only) row, and check if there is any data.
      if (!rs.next()) {
        return Optional.empty();
      }

      // There is data

      // The type correspondences between Java and MySQL
      // can be consulted in this table
      // https://dev.mysql.com/doc/connector-j/en/connector-j-reference-type-conversions.html
      return Optional.of(new Customer(
          rs.getObject("id", BigInteger.class), // BIGINT UNSIGNED (SERIAL)
          rs.getString("first_name"),
          rs.getString("last_name"),
          rs.getInt("age"),
          rs.getObject("registered_at", LocalDateTime.class))); // DATETIME

      // An error occurred
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    // User not found
    return Optional.empty();
  }

  public Optional<Customer> get(long id) {
    return this.get(BigInteger.valueOf(id));
  }

  public ArrayList<Customer> getAll() {
    String sql = "SELECT * FROM customer";
    ArrayList<Customer> customerList = new ArrayList<>();

    Connection conn = ConnectionManager.INSTANCE.getDbConnection();

    try (
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {

      while (rs.next()) {
        customerList.add(new Customer(
          rs.getObject("id", BigInteger.class), // BIGINT UNSIGNED (SERIAL)
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getInt("age"),
            rs.getObject("registered_at", LocalDateTime.class))); // DATETIME
      }

      // An error occurred
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    return customerList;
  }

  public void save(Customer customer) {

  }

  public void update(Customer user, String[] params) {

  }

}
