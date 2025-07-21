package com.davidelatina.bankingdemo.model.service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.davidelatina.bankingdemo.dao.CustomerDAO;
import com.davidelatina.bankingdemo.model.entity.Customer;

public class CustomerService {
  // Static variables
  // --- Hashing
  private final int saltWidth = 16;
  private final int iterationCount = 1000; // for generating PBEKey
  private final int keyLengthBits = 512; // for SHA512 hashing
  private final String algorithm = "PBKDF2WithHmacSHA512";

  // Instance variables
  private final CustomerDAO customerDAO;
  private final SecureRandom secureRandom;

  // Constructor
  public CustomerService(CustomerDAO customerDAO, SecureRandom secureRandom) {
    this.customerDAO = customerDAO;
    this.secureRandom = secureRandom;
  }

  // Methods

  // public Customer authenticateUser(String username, String password) {
  //
  // }

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

  public void createNewCustomer(String username, char[] password, String firstName, String lastName, int age)
      throws RuntimeException, SQLException {

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
    byte[] salt = new byte[saltWidth];

    secureRandom.nextBytes(salt);

    // Hash password
    byte[] hashedPassword;
    try {
      hashedPassword = hashPassword(salt, password);
    } catch (RuntimeException ex) {
      throw ex;
    }

    // Register user in database
    try {
      this.customerDAO.createCustomer(username, firstName, lastName, age, salt, hashedPassword);
    } catch (SQLException ex) {
      throw ex;
    }

  }

  private byte[] hashPassword(byte[] salt, char[] password) throws RuntimeException {

    // Instantiate PBEKeySpec. (will clone password and salt)
    PBEKeySpec spec = new PBEKeySpec(password, salt, iterationCount, keyLengthBits);

    // Clear password
    Arrays.fill(password, '\u0000');

    // Obtain a SecretKeyFactory instance
    SecretKeyFactory skf;
    try {
      skf = SecretKeyFactory.getInstance(algorithm);

    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException("Hashing error: unknown algorithm", ex);
    }

    // Derive key
    SecretKey key;
    try {
      key = skf.generateSecret(spec);

    } catch (InvalidKeySpecException ex) {
      throw new RuntimeException("Hashing error: invalid keyspec", ex);
    }

    // Extract hashed password bytes
    byte[] hashedPassword = key.getEncoded();

    return hashedPassword;
  }

}
