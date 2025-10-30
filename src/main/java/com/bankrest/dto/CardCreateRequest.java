package com.bankrest.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CardCreateRequest(
  @NotNull Long ownerId,
  @Pattern(regexp="\\d{16}") String cardNumber,
  @Min(1) @Max(12) Integer expiryMonth,
  @Min(2024) Integer expiryYear,
  @DecimalMin("0.00") BigDecimal initialBalance
) {}