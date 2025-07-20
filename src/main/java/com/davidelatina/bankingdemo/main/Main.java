package com.davidelatina.bankingdemo.main;

import com.davidelatina.bankingdemo.controller.BankController;

public class Main {
  public static void main(String[] args) {

    BankController bankController = new BankController();

    bankController.run();

  }

}