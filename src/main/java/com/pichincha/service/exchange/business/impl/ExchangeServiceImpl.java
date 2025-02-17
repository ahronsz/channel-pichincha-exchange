package com.pichincha.service.exchange.business.impl;

import com.pichincha.service.exchange.business.ExchangeService;
import com.pichincha.service.exchange.business.UserService;
import com.pichincha.service.exchange.business.mapper.ExchangeMapper;
import com.pichincha.service.exchange.client.ExternalServiceClient;
import com.pichincha.service.exchange.client.model.ExternalUserResponse;
import com.pichincha.service.expose.dto.request.ExchangeRequest;
import com.pichincha.service.expose.dto.response.ExchangeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ExchangeServiceImpl implements ExchangeService {

    private final ExternalServiceClient externalServiceClient;
    private final UserService userService;
    private final ExchangeMapper exchangeMapper;

    @Override
    public Mono<Void> createExchange(ExchangeRequest exchangeRequest, String token) {
        return auth(exchangeRequest.userId(), token)
                .flatMap(userResponse -> externalServiceClient.createExchange(exchangeRequest));
    }

    @Override
    public Mono<Void> updateExchange(Long id, ExchangeRequest exchangeRequest, String token) {
        return auth(exchangeRequest.userId(), token)
                .flatMap(externalUserResponse -> externalServiceClient.updateExchange(id, exchangeRequest));
    }

    @Override
    public Flux<ExchangeResponse> listExchangeByUserId(Long userId, String token) {
        return auth(userId, token)
                .flatMapMany(userResponse -> externalServiceClient.listExchangeByUserId(userId)
                        .map(externalExchangeResponse -> exchangeMapper.toResponse(externalExchangeResponse, userResponse.name())));
    }

    @Override
    public Mono<ExchangeResponse> getExchangeById(Long id, Long userId, String token) {
        return auth(userId, token)
                .flatMap(externalUserResponse ->
                        externalServiceClient.getExchangeById(id)
                                .map(externalExchangeResponse ->
                                        exchangeMapper.toResponse(externalExchangeResponse, externalUserResponse.name())));
    }

    private Mono<ExternalUserResponse> auth(Long userId, String token) {
        return userService.findUserById(userId, token);
    }
}
