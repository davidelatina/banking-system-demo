package com.davidelatina.bankingdemo.controller;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.davidelatina.bankingdemo.model.entity.Customer;
import com.davidelatina.bankingdemo.model.service.CustomerService;
import com.davidelatina.bankingdemo.model.service.SessionManager;
import com.davidelatina.bankingdemo.view.dto.Menu;
import com.davidelatina.bankingdemo.view.impl.MenuView;

/**
 * The {@code BankController} class acts as central orchestrator for the
 * application,
 * adhering to the Controller role in a Model-View-Controller (MVC)
 * design pattern.
 *
 * @see com.davidelatina.bankingdemo.view.impl.MenuView
 * @see com.davidelatina.bankingdemo.view.dto.Menu
 */
public class BankController {

  // Instance variables
  private final MenuView menuView;
  private final SessionManager sessionManager;
  private final CustomerService customerService;

  // Constructor
  public BankController(MenuView menuView, SessionManager sessionManager, CustomerService customerService) {
    this.menuView = menuView;
    this.sessionManager = sessionManager;
    this.customerService = customerService;
  }

  // Methods
  public void run() {

    int userSelection = -1;

    while (true) { // Main application loop. Exit on user input in main menu

      try {
        userSelection = menuView.menu(MenuDefinitions.mainMenu);
      } catch (NoSuchElementException ex) {
        menuView.displayError(ex.getMessage());
        System.exit(1);
      }

      if (userSelection == MenuDefinitions.mainMenu.option().length) {
        break; // <-------------------- EXIT MAIN LOOP, FUNCTION AND APPLICATION
      }

      // Process user selection
      switch (userSelection) {
        case 1 -> { // Auditor mode
          auditorMode();
        }

        case 2 -> { // Log in as customer
          try {
            if (loginUser()) {
              userMenu();
            }
          } catch (Exception ex) {
            menuView.displayError(ex.getMessage());
          }
        }

        case 3 -> { // Register as new customer
          try {
            registerUser();
          } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
          }
          
        }

        default -> {
          menuView.displayError("Unsupported menu operation.");
        }
      }
    }
  }

  private void userMenu() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'userMenu'");
  }

  private void auditorMode() {

    int userSelection = -1;

    // Main program loop
    while (true) {

      try {
        userSelection = menuView.menu(MenuDefinitions.auditorMenu);
      } catch (NoSuchElementException ex) {
        menuView.displayError(ex.getMessage());
        userSelection = MenuDefinitions.auditorMenu.option().length;
      }

      if (userSelection == MenuDefinitions.auditorMenu.option().length) {
        break; // <--------------------------------------------- EXIT WHILE LOOP
      }

      switch (userSelection) {

        case 1 -> {
          viewSingleCustomer();
        }

        case 2 -> { // View customer list
          try {
            customerService.getFullCustomerList()
                .forEach(customer -> menuView.displayMessage(customer.toString()));
          } catch (Exception ex) {
            menuView.displayError(ex.getMessage());
          }
        }

        default -> {
          menuView.displayError("Error in menu selection.");
        }
      }
    }
  }




  // --- Subroutines

  private void viewSingleCustomer() {
    // View single customer

    // Read ID from user input
    BigInteger userSelectedId;
    try {
      userSelectedId = new BigInteger(menuView.userSelectedStringAny("Insert customer ID"));
    } catch (NumberFormatException ex) {
      menuView.displayError(ex.getMessage());
      return;
    }

    // Search for the corresponding customer, if there is one
    Optional<Customer> customer;
    try {
      customer = customerService.viewSingleCustomer(userSelectedId);
    } catch (Exception ex) {
      menuView.displayError(ex.getMessage());
      return;
    }

    // Display information
    if (customer.isEmpty()) {
      menuView.displayMessage("User not found.");
    } else {
      menuView.displayMessage(customer.get().toString());
    }
  }

  private boolean loginUser() throws SQLException {

    String username;
    char[] password;

    int attempts = 3;
    do {
      username = menuView.userSelectedStringAny("Enter username");
      password = menuView.readPassword("Enter password");

      // Check if customer by this username is registered
      if (customerService.checkUsernameInUse(username)) {

        // Attempt to log in
        if (sessionManager.login(username, password)) {
          menuView.displayMessage("Welcome back, " +
              sessionManager.getActiveUser().firstName());
          return true;
        }
      }

      // Delete first password and prompt the user again
      Arrays.fill(password, '\u0000');
      menuView.displayMessage("Username and Password do not match.");
      attempts--;
    } while (attempts > 0);

    menuView.displayError("Too many attempts.");
    return false;
  }

  private void registerUser() {

    String username = menuView.userSelectedStringAny("Enter username");

    char[] password;
    char[] repeated;
    int attempts = 3;
    do {
      password = menuView.readPassword("Enter password");
      repeated = menuView.readPassword("Repeat password");

      boolean passwordsMatch = Arrays.equals(password, repeated);

      // Destroy repeated password
      Arrays.fill(repeated, '\u0000');

      // Exit loop if passwords matched
      if (passwordsMatch) {
        break;
      }

      // Otherwise, delete first password as well and inform the user
      Arrays.fill(password, '\u0000');
      menuView.displayMessage("Passwords do not match");
      attempts--;
    } while (attempts > 0);

    if (attempts <= 0) {
      menuView.displayError("Too many attempts.");
      return;
    }

    menuView.displayMessage("Passwords match");

    String firstName = menuView.userSelectedStringAny("Enter first name");
    String lastName = menuView.userSelectedStringAny("Enter last name");
    int age = menuView.userSelectedInt("Enter age", "Enter an integer");

    try {
      this.customerService.createNewCustomer(username, password, firstName, lastName, age);
    } catch (Exception ex) {
      ex.printStackTrace();
      menuView.displayError(ex.getMessage());
    } finally {
      Arrays.fill(password, '\u0000');
    }
  }

}

/**
 * Utility class. Holds menu definitions for {@link BankController}.
 */
final class MenuDefinitions {

  // Constructor must not be called.
  private MenuDefinitions() {
    throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  // Menu definitions

  public static final Menu mainMenu = new Menu(
      "   --- BANKING SYSTEM DEMO ---",
      new String[] {
          "Enter auditor mode (no login, full access)",
          "Log in as customer",
          "Register as new customer",
          "Exit"
      },
      "Selection",
      "Please select a valid option.");

  public static final Menu auditorMenu = new Menu(
      "   --- AUDITOR MODE ---",
      new String[] {
          "View single customer",
          "View customer list",
          "Exit"
      },
      "Selection",
      "Please select a valid option.");

}
