package com.bankrest.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record TransferRequest(@NotNull Long fromCardId, @NotNull Long toCardId,
                              @DecimalMin("0.01") BigDecimal amount) {}