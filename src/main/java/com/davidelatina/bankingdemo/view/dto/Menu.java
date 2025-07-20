package com.davidelatina.bankingdemo.view.dto;

public record Menu(
    String title, String[] option, String prompt, String errMessage) {
}
