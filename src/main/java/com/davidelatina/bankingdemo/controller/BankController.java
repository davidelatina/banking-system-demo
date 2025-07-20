package com.davidelatina.bankingdemo.controller;

import java.math.BigInteger;
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
 * adhering to the Controller role in the Model-View-Controller (MVC)
 * design pattern.
 *
 * @see com.davidelatina.bankingdemo.view.impl.MenuView
 * @see com.davidelatina.bankingdemo.view.dto.Menu
 */
public class BankController {

  // Instance variables
  private final MenuView menuView;
  private final CustomerService customerService;

  // Constructor
  public BankController(MenuView menuView, SessionManager sessionManager, CustomerService customerService) {
    this.menuView = menuView;
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
        continue;
      }

      if (userSelection == MenuDefinitions.mainMenu.option().length) {
        break; // <-------------------- EXIT MAIN LOOP, FUNCTION AND APPLICATION
      }

      // Process user selection
      switch (userSelection) {
        case 1: // Auditor mode
          auditorMode();
          break;

        case 2: // Log in as customer
          menuView.displayError("Operation currently unsupported.");
          break;

        case 3: // Register as new customer
          menuView.displayError("Operation currently unsupported.");
          break;

        default:
          menuView.displayError("Unsupported menu operation.");
      }
    }
  }

  private void auditorMode() {

    int userSelection = -1;

    // Main program loop
    while (true) {

      try {
        userSelection = menuView.menu(MenuDefinitions.auditorMenu);
      } catch (NoSuchElementException ex) {
        menuView.displayError(ex.getMessage());
      }

      if (userSelection == MenuDefinitions.auditorMenu.option().length) {
        break; // <--------------------------------------------- EXIT WHILE LOOP
      }

      switch (userSelection) {

        case 1 -> { // View single customer

          // Read ID from user input
          BigInteger userSelectedId;
          try {
            userSelectedId = new BigInteger(menuView.userSelectedStringAny("Insert customer ID"));
          } catch (NumberFormatException ex) {
            menuView.displayError(ex.getMessage());
            continue;
          }

          // Search for the corresponding customer, if there is one
          Optional<Customer> customer;
          try {
            customer = customerService.viewSingleCustomer(userSelectedId);
          } catch (Exception ex) {
            menuView.displayError(ex.getMessage());
            continue;
          }

          // Display information
          if (customer.isEmpty()) {
            menuView.displayMessage("User not found.");
          } else {
            menuView.displayMessage(customer.get().toString());
          }
        }

        case 2 -> { // View customer list

          try {
            customerService.getFullCustomerList()
                .forEach(customer -> menuView.displayMessage(customer.toString()));
          } catch (Exception ex) {
            menuView.displayError(ex.getMessage());
          }

        }

        case 3 -> { // Add customer

          String username = menuView.userSelectedStringAny("Enter username");

          String password;
          int attempts = 3;
          do {
            password = menuView.userSelectedStringAny("Enter password");
            if (menuView.userSelectedStringAny("Repeat password").equals(password)) {
              break;
            }
            menuView.displayMessage("Passwords do not match");
            attempts--;
          } while (attempts > 0);
          if (attempts == 0) {
            // Clear password
            password = null;
            System.gc();
            menuView.displayError("Too many attempts.");
            continue;
          }

          String firstName = menuView.userSelectedStringAny("Enter first name");
          String lastName = menuView.userSelectedStringAny("Enter last name");
          int age = menuView.userSelectedInt("Enter age", "Enter an integer");

          try {
            this.customerService.createNewCustomer(username, password.toCharArray(), firstName, lastName, age);
          } catch (Exception ex) {
            menuView.displayError(ex.getMessage());
          } finally {
            password = null;
            System.gc();
          }
        }

        default -> {
          menuView.displayError("Error in menu selection.");
        }
      }
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
          "Add customer",
          "Exit"
      },
      "Selection",
      "Please select a valid option.");

}
