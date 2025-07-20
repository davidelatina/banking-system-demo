package com.davidelatina.bankingdemo.controller;

import java.math.BigInteger;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.davidelatina.bankingdemo.dao.CustomerDAO;
import com.davidelatina.bankingdemo.model.entity.Customer;
import com.davidelatina.bankingdemo.view.dto.Menu;
import com.davidelatina.bankingdemo.view.impl.MenuView;



/**
 * The {@code BankController} class acts as central orchestrator for the application,
 * adhering to the Controller role in the Model-View-Controller (MVC)
 * design pattern.
 *
 * @see com.davidelatina.bankingdemo.view.impl.MenuView
 * @see com.davidelatina.bankingdemo.view.dto.Menu
 */
public class BankController {

  public void run() {

    int userSelection = -1;

    while (true) { // Main application loop. Exit on user input in main menu

      // Determine current application state

      // Decide which menu to present

      // Call MenuView to display the menu and get input

      try {
        userSelection = MenuView.INSTANCE.menu(MenuDefinitions.mainMenu);
      } catch (NoSuchElementException ex) {

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
          MenuView.INSTANCE.displayError("Operation currently unsupported.");
          break;

        case 3: // Register as new customer
          MenuView.INSTANCE.displayError("Operation currently unsupported.");
          break;

        default:
          MenuView.INSTANCE.displayError("Unsupported menu operation.");
      }

      // Update application state based on the selection

      // Break the loop if an "Exit" option is chosen.
    }

  }

  private void auditorMode() {

    int userSelection = -1;
    BigInteger userSelectedId;

    // Main program loop
    while (true) {

      try {
        userSelection = MenuView.INSTANCE.menu(MenuDefinitions.auditorMenu);
      } catch (NoSuchElementException ex) {

      }

      if (userSelection == MenuDefinitions.auditorMenu.option().length) {
        break; // <--------------------------------------------- EXIT WHILE LOOP
      }

      switch (userSelection) {

        case 1: // View single customer
          userSelectedId = BigInteger
              .valueOf((long) MenuView.INSTANCE.userSelectedInt("Select customer id", "Select a valid integer.")); 
          {
            Optional<Customer> customer = CustomerDAO.INSTANCE.get(userSelectedId);
            if (customer.isEmpty()) {
              System.out.println("User not found.");
            } else {
              System.out.println(customer.get().toString());
            }
          }
          break;

        case 2: // View customer list
          CustomerDAO.INSTANCE.getAll()
              .forEach(
                  customer -> MenuView.INSTANCE.displayMessage(customer.toString()));
          break;
      }
    }
  }
}

/**
 * Utility class. Holds menu definitions for @see BankController.
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
