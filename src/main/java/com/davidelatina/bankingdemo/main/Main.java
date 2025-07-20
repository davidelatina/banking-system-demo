package com.davidelatina.bankingdemo.main;

import com.davidelatina.bankingdemo.controller.BankController;
import com.davidelatina.bankingdemo.dao.CustomerDAO;
import com.davidelatina.bankingdemo.model.service.CustomerService;
import com.davidelatina.bankingdemo.view.impl.MenuView;

public class Main {
  public static void main(String[] args) {

    // Initialize BankController
    BankController bankController = new BankController(
        MenuView.INSTANCE,
        new CustomerService(
            CustomerDAO.INSTANCE));

    // Start application
    bankController.run();

  }

}