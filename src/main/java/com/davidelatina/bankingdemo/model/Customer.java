package com.davidelatina.bankingdemo.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;

public record Customer(BigInteger id, String firstName, String lastName, int age, LocalDateTime datetime) {
  public Customer {
    // ID will be null when creating a new user: the DBMS will handle auto-incremented IDs.

    // Allowing for mononyms, at least one among first name and last name must be non-null
    if (firstName == null && lastName == null) {
      throw new NullPointerException(
          "Cannot create a Customer with no first name nor last name.");
    }
 
    Objects.requireNonNull(age);
    Objects.requireNonNull(datetime);
  }
}
