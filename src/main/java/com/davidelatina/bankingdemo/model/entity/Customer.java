package com.davidelatina.bankingdemo.model.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

public record Customer(BigInteger id, String username, String firstName, String lastName, int age, LocalDateTime datetime) {}
