package com.pichincha.service.expose.dto.request;

import com.pichincha.service.exchange.enums.CurrencyEnum;
import com.pichincha.service.expose.validator.ValueOfEnumCurrency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ExchangeRequest(
        @NotNull()
        @Min(value = 1)
        Long userId,

        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 10, fraction = 2)
        @NotNull
        Double amount,

        @ValueOfEnumCurrency(enumClass = CurrencyEnum.class)
        String sourceCurrency,

        @ValueOfEnumCurrency(enumClass = CurrencyEnum.class)
        String targetCurrency,

        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 10, fraction = 4)
        Double exchangeRate
) {}
