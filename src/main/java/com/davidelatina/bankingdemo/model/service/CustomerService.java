package com.davidelatina.bankingdemo.model.service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.davidelatina.bankingdemo.dao.CustomerDAO;
import com.davidelatina.bankingdemo.model.entity.Customer;

public class CustomerService {

  // Instance variables
  private final CustomerDAO customerDAO;
  private final SecureRandom secureRandom;

  // Constructor
  public CustomerService(CustomerDAO customerDAO, SecureRandom secureRandom) {
    this.customerDAO = customerDAO;
    this.secureRandom = secureRandom;
  }

  // Methods

  //public Customer authenticateUser(String username, String password) {
  //
  //}

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

  public void createNewCustomer(String username, char[] password, String firstName, String lastName, int age) throws RuntimeException, SQLException {

    // Check if username is available


    // Check if password satisfies requirements


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


    // Generate salt
    byte[] salt = new byte[16];

    secureRandom.nextBytes(salt);

    // --- Hashing
    int iterationCount = 1000;
    int keyLengthBits = 512;
    // Instantiate PBEKeySpec
    PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLengthBits);

    // Obtain a SecretKeyFactory instance
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

    // Derive key
    SecretKey key = skf.generateSecret(spec);

    // Extract hashed password bytes
    byte[] hashedPassword = key.getEncoded();

    // Clear password
    for (int i = 0; i < password.length; i++) {
      password[i] = 0;
    }
    


    // Registering user in database
    try {
      this.customerDAO.createCustomer(username, firstName, lastName, age, salt, hashedPassword);
    } catch (SQLException ex) {
      throw ex;
    }

  }
}
