package com.davidelatina.bankingdemo.main;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.Optional;

import com.davidelatina.bankingdemo.util.ConnectionManager;
import com.davidelatina.bankingdemo.dao.*;
import com.davidelatina.bankingdemo.model.*;

public class Main {
  public static void main(String[] args) {

    // Start connection
    ConnectionManager.init();

    Scanner scanStr = new Scanner(System.in);
    Scanner scanNum = new Scanner(System.in);

    int userSelection = -1;
    BigInteger userSelectedId;

    // Main program loop
    while (true) {

      System.out.println("   DEMO BANK");
      System.out.println("0. Exit");
      System.out.println("1. View single customer");
      System.out.println("2. View customer list");

      userSelection = scanNum.nextInt();
      scanNum.nextLine(); // Flush newline

      if (userSelection == 0) { // <---------------------------- EXIT WHILE LOOP
        break;
      }

      switch (userSelection) {

        case 1: // View single customer
          System.out.print("Select customer id: ");
          userSelectedId = scanNum.nextBigInteger();
          scanNum.nextLine(); {
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
              .forEach(customer -> System.out.println(customer.toString()));
          break;
      }
    }

    scanStr.close();
    scanNum.close();
  }

}