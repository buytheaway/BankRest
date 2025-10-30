package com.bankrest.dto;
import com.bankrest.domain.CardStatus; import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record CardCreateRequest(
  @NotNull Long ownerId,
  @Pattern(regexp="\\d{16}") String cardNumber,
  @Min(1) @Max(12) Integer expiryMonth,
  @Min(2024) Integer expiryYear,
  @DecimalMin(value="0.00") BigDecimal initialBalance
) {}
public record CardResponse(Long id, String maskedNumber, Integer expiryMonth, Integer expiryYear, CardStatus status, boolean blockRequested, BigDecimal balance) {}
public record TransferRequest(@NotNull Long fromCardId, @NotNull Long toCardId, @DecimalMin("0.01") BigDecimal amount) {}
