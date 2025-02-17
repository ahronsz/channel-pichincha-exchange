package com.pichincha.service.exchange.business.mapper;

import com.pichincha.service.exchange.client.model.ExternalExchangeResponse;
import com.pichincha.service.expose.dto.response.ExchangeResponse;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface ExchangeMapper {

    ExchangeResponse toResponse(ExternalExchangeResponse externalExchangeResponse, String userName);
}
