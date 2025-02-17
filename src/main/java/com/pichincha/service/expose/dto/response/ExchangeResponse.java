package com.pichincha.service.expose.dto.response;

import java.time.LocalDateTime;

public record ExchangeResponse(
    Long id,
    String userName,
    Double amount,
    String sourceCurrency,
    String targetCurrency,
    Double convertedAmount,
    Double exchangeRate,
    LocalDateTime updatedAt,
    LocalDateTime createdAt
) {
}
