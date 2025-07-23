package com.davidelatina.bankingdemo.model.service;

import java.sql.SQLException;

import com.davidelatina.bankingdemo.model.entity.Customer;

/**
 * The {@code SessionManager} class is a singleton responsible for managing the
 * active user session
 * throughout the banking application.
 *
 * <p>
 * It holds the state of the currently logged-in {@link Customer} and provides
 * methods for logging in, logging out, and retrieving the active user's
 * information.
 * This class ensures that only one user can be active at a time within the
 * application's session.
 * </p>
 *
 * <p>
 * <b>Usage:</b>
 * </p>
 * <ul>
 * <li>The {@link com.davidelatina.bankingdemo.controller.BankController} will
 * primarily
 * interact with this manager to initiate login and logout operations based on
 * user input.</li>
 * <li>Other service classes within the {@code model/service} layer may query
 * this manager
 * to obtain the {@link #getActiveUser() active user}'s context for business
 * logic
 * (e.g., permission checks, associating transactions with the current
 * user).</li>
 * </ul>
 *
 * @see Customer
 * @see com.davidelatina.bankingdemo.controller.BankController
 */
public enum SessionManager {

  INSTANCE;

  // Instance variables
  private CustomerService customerService;
  private Customer activeUser;

  // Methods
  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }

  public Customer getActiveUser() {
    return activeUser;
  }


  public boolean login(String username, char[] password) throws IllegalArgumentException, SQLException {
    if (customerService.authenticateUser(username, password)) {
      activeUser = customerService.getCustomer(username);
      return true;
    }
    return false;
  }

  public void logout() {
    activeUser = null;
  }

}
