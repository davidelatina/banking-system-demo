package com.davidelatina.bankingdemo.model.service;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.davidelatina.bankingdemo.dao.CustomerDAO;
import com.davidelatina.bankingdemo.model.entity.Customer;

public class CustomerService {
  // Static variables
  // --- Hashing
  private static final int SALT_WIDTH = 16;
  private static final int ITERATION_COUNT = 1000; // for generating PBEKey
  private static final int KEY_LENGTH_BITS = 512; // for SHA512 hashing
  private static final String HASHING_ALGORITHM = "PBKDF2WithHmacSHA512";

  // Instance variables
  private final CustomerDAO customerDAO;
  private final SecureRandom secureRandom;

  // Constructor
  public CustomerService(CustomerDAO customerDAO, SecureRandom secureRandom) {
    this.customerDAO = customerDAO;
    this.secureRandom = secureRandom;
  }

  // Methods

  public Customer getCustomer(String username) throws SQLException {

    BigInteger id = customerDAO.getIdFromUsername(username);

    return customerDAO.get(id).orElseThrow();
  }

  public boolean authenticateUser(String username, char[] password) throws IllegalArgumentException, SQLException {
    // --- Parameter verification
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null.");
    }

    if (password == null) {
      throw new IllegalArgumentException("Password cannot be null.");
    }

    // TODO: retrieve salt and hashed password in a single query
    byte[] salt = customerDAO.getSalt(username);
    byte[] newHashedPassword = hashPassword(salt, password);
    Arrays.fill(password, '\u0000');

    byte[] oldHashedPassword = customerDAO.getHashedPassword(username);

    return Arrays.equals(oldHashedPassword, newHashedPassword);
  }

  public List<Customer> getFullCustomerList() throws SQLException {
    return customerDAO.getAll();
  }

  public Optional<Customer> viewSingleCustomer(BigInteger id) throws IllegalArgumentException, SQLException {

    // id is equal to or lesser than zero
    if (id.compareTo(BigInteger.valueOf(0)) <= 0) {
      throw new IllegalArgumentException("Invalid Customer ID. Must be greater than 0");
    }

    return customerDAO.get(id);
  }

  public boolean checkUsernameAvailable(String username) throws SQLException {
    return customerDAO.checkUsernameAvailable(username);
  }

  public boolean checkUsernameInUse(String username) throws SQLException {
    return !checkUsernameAvailable(username);
  }

  public void createNewCustomer(String username, char[] password, String firstName, String lastName, int age)
      throws RuntimeException, SQLException {
    // --- Parameter verification
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be null.");
    }

    // Check if username is available
    if (!customerDAO.checkUsernameAvailable(username)) {
      throw new IllegalArgumentException("Username unavailable.");
    }

    // TODO: Check if password satisfies requirements

    // ID will be null when creating a new user: the DBMS will handle
    // auto-incremented IDs.

    // Name
    // Allowing for mononyms, at least one among first name and last name must be
    // present
    if (firstName.isBlank() && lastName.isBlank()) {
      throw new IllegalArgumentException(
          "Cannot create a Customer with neither first name nor last name.");
    }

    // Remove leading and trailing whitespace
    firstName = firstName.trim();
    lastName = lastName.trim();

    // Age
    if (age < 0) {
      throw new IllegalArgumentException("A customer's age cannot be negative.");
    }

    // datetime will be null when creating a new user: the DBMS will register the
    // exact time it registered the new user.

    // Generate salt
    byte[] salt = new byte[SALT_WIDTH];

    secureRandom.nextBytes(salt);

    // Hash password
    byte[] hashedPassword;

    // Throws RuntimeException
    hashedPassword = hashPassword(salt, password);

    // Register user in database
    // Throws SQLException
    this.customerDAO.createCustomer(username, firstName, lastName, age, salt, hashedPassword);
  }

  private byte[] hashPassword(byte[] salt, char[] password) throws RuntimeException {

    // Instantiate PBEKeySpec. (will clone password and salt)
    PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATION_COUNT, KEY_LENGTH_BITS);

    // Clear password
    Arrays.fill(password, '\u0000');

    // Obtain a SecretKeyFactory instance
    SecretKeyFactory skf;
    try {
      skf = SecretKeyFactory.getInstance(HASHING_ALGORITHM);

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
