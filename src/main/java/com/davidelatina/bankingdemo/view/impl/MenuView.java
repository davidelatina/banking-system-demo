package com.davidelatina.bankingdemo.view.impl;

import java.util.NoSuchElementException;
import java.util.Scanner;

import com.davidelatina.bankingdemo.view.dto.Menu;

/**
 * Terminal user interface for the program.
 */
public enum MenuView {

  INSTANCE;

  // ANSI terminal codes
  // Reset text
  private static final String RESET = "\033[0m";
  // Colors
  private static final String BLACK = "\033[0;30m";
  private static final String RED = "\033[0;31m";

  // Scanners
  private Scanner scanNum;
  private Scanner scanStr;

  private MenuView() {
    this.scanNum = new Scanner(System.in);
    this.scanStr = new Scanner(System.in);
  }

  /**
   * Display a terminal integer selection menu, loop until valid user selection.
   * <p>
   * Show the user options from 1 to n, each taken from @see Menu.option
   * 
   * @param menu @see Menu record
   * @return int userSelection
   * @throws RuntimeException for Scanner exceptions
   */
  public int menu(Menu menu) throws RuntimeException {

    // Size of menu options array
    int size = menu.option().length;

    int userSelection = -1;

    while (true) { // Continuous selection loop. Exit upon valid selection.

      // Print menu text
      System.out.println(menu.title());
      for (int i = 0; i < size; i++) {
        System.out.println(i + 1 + ". " + menu.option()[i]);
      }
      System.out.print(menu.prompt() + ": ");

      // Catch Scanner exceptions
      try {

        // Save user selection, attempt to translate to int
        userSelection = Integer.parseInt(scanNum.nextLine());

        // nextLine: Scanner is closed
      } catch (IllegalStateException e) {
        System.err.println(e.getMessage());
        this.scanNum = new Scanner(System.in);

        continue;

        // nextLine: Input is exhausted
      } catch (NoSuchElementException e) {
        System.out.println(e.getMessage());

        throw e;

        // parseInt: User inserted a non-number or a non-integer
      } catch (NumberFormatException e) {

        System.out.println(menu.errMessage() + "\n");

        continue;
      }

      // Verify input
      if (1 <= userSelection && userSelection <= size) {
        return userSelection; // <------------------------- LOOP AND METHOD EXIT
      }

      // Input outside range, retrying
      System.out.println(menu.errMessage() + "\n");
    }
  }

  /**
   * Display a terminal integer selection menu, accepting any user selection that
   * is an integer.
   * <p>
   * Show the user options from 1 to n, each taken from @see Menu.option
   * 
   * @param menu {@link Menu} record
   * @return int userSelection
   * @throws RuntimeException for Scanner exceptions
   */
  public int menuIntAny(Menu menu) throws RuntimeException {

    // Size of menu options array
    int size = menu.option().length;

    int userSelection = -1;

    while (true) { // Continuous selection loop. Exit upon any integer selection.

      // Print menu text
      System.out.println(menu.title());
      for (int i = 0; i < size; i++) {
        System.out.println(i + 1 + ". " + menu.option()[i]);
      }
      System.out.print(menu.prompt() + ": ");

      // Catch Scanner exceptions
      try {

        // Save user selection, attempt to translate to int
        userSelection = Integer.parseInt(scanNum.nextLine());

        // nextLine: Scanner is closed
      } catch (IllegalStateException e) {
        System.err.println(e.getMessage());
        this.scanNum = new Scanner(System.in);

        continue;

        // nextLine: Input is exhausted
      } catch (NoSuchElementException e) {
        System.out.println(e.getMessage());

        throw e;

        // parseInt: User inserted a non-number or a non-integer
      } catch (NumberFormatException e) {

        System.out.println(menu.errMessage() + "\n");

        continue;
      }

      return userSelection; // <------------------------- LOOP AND METHOD EXIT

    }
  }

  public int userSelectedInt(String prompt, String errMessage) {
    return this.menuIntAny(new Menu("", new String[]{}, prompt, errMessage));
  }

  public String userSelectedStringAny(String prompt) {
    System.out.print(prompt + ": ");
    return scanStr.nextLine();
  }

  public void displayMessage(String message) {
    System.out.println(message);
  }

  public void displayError(String message) {
    System.err.println(RED + message + RESET);
  }
}
