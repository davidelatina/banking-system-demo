package com.davidelatina.bankingdemo.main;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import com.davidelatina.bankingdemo.controller.BankController;
import com.davidelatina.bankingdemo.dao.CustomerDAO;
import com.davidelatina.bankingdemo.dao.util.ConnectionManager;
import com.davidelatina.bankingdemo.model.service.CustomerService;
import com.davidelatina.bankingdemo.model.service.SessionManager;
import com.davidelatina.bankingdemo.view.impl.MenuView;

public class Main {
  public static void main(String[] args) {

    // Initialize CustomerDAO
    CustomerDAO customerDAO = new CustomerDAO(ConnectionManager.INSTANCE);

    // Initialize customer service
    CustomerService customerService;
    try {
      customerService = new CustomerService(
          customerDAO, 
          SecureRandom.getInstanceStrong()); // Throws NoSuchAlgorithmException
    } catch (NoSuchAlgorithmException e) {
      MenuView.INSTANCE.displayError("No algorithm for secure random number generation is available on this platform. Exiting application...");
      return;
    }
    
    // Fulfill CustomerService dependency for SessionManager
    SessionManager.INSTANCE.setCustomerService(customerService);

    // Initialize BankController
    BankController bankController = new BankController(
        MenuView.INSTANCE,
        SessionManager.INSTANCE,
        customerService);

    // Start application
    bankController.run();

  }

}