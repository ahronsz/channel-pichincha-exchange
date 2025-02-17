package com.pichincha.service.exchange.client.model;

import java.time.LocalDateTime;

public record ExternalExchangeResponse(
    Long id,
    Long userId,
    Double amount,
    String sourceCurrency,
    String targetCurrency,
    Double convertedAmount,
    Double exchangeRate,
    LocalDateTime updatedAt,
    LocalDateTime createdAt
) {
}
