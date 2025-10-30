package com.bankrest.dto;
import com.bankrest.domain.CardStatus;
import java.math.BigDecimal;
public record CardResponse(Long id, String maskedNumber, Integer expiryMonth, Integer expiryYear,
                           CardStatus status, boolean blockRequested, BigDecimal balance) {}