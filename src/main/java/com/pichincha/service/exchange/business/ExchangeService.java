package com.pichincha.service.exchange.business;

import com.pichincha.service.expose.dto.request.ExchangeRequest;
import com.pichincha.service.expose.dto.response.ExchangeResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeService {
    Mono<Void> createExchange(ExchangeRequest exchangeRequest, String token);
    Mono<Void> updateExchange(Long id, ExchangeRequest exchangeUpdateRequest, String token);
    Flux<ExchangeResponse> listExchangeByUserId(Long userId, String token);
    Mono<ExchangeResponse> getExchangeById(Long id, Long userId, String token);
}
