package com.davidelatina.bankingdemo.model.service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import com.davidelatina.bankingdemo.dao.CustomerDAO;
import com.davidelatina.bankingdemo.model.entity.Customer;

public class CustomerService {

  // Instance variables
  private final CustomerDAO customerDAO;

  // Constructor
  public CustomerService(CustomerDAO customerDAO) {
    this.customerDAO = customerDAO;
  }

  // Methods

  public ArrayList<Customer> getFullCustomerList() throws SQLException {
    try {
      return customerDAO.getAll();
    } catch (SQLException ex) {
      throw ex;
    }
  }


  public Optional<Customer> viewSingleCustomer(BigInteger id) throws RuntimeException, SQLException {

    // id is equal to or lesser than zero
    if (id.compareTo(BigInteger.valueOf(0)) <= 0) {
      throw new RuntimeException("Invalid Customer ID. Must be greater than 0");
    }

    try {
      return customerDAO.get(id);
    } catch (SQLException ex) {
      throw ex;
    }
  }

  public void createNewCustomer(String firstName, String lastName, int age) throws RuntimeException, SQLException {

    // ID will be null when creating a new user: the DBMS will handle
    // auto-incremented IDs.

    // Name
    // Allowing for mononyms, at least one among first name and last name must be
    // present
    if (firstName.isBlank() && lastName.isBlank()) {
      throw new RuntimeException(
          "Cannot create a Customer with neither first name nor last name.");
    }

    // Remove leading and trailing whitespace
    firstName = firstName.trim();
    lastName = lastName.trim();

    // Age
    if (age < 0) {
      throw new RuntimeException("A customer's age cannot be negative.");
    }

    // datetime will be null when creating a new user: the DBMS will register the
    // exact time it registered the new user.

    // Registering user in database
    try {
      this.customerDAO.createCustomer(firstName, lastName, age);
    } catch (SQLException ex) {
      throw ex;
    }

  }
}
