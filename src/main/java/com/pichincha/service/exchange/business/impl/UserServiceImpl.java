package com.pichincha.service.exchange.business.impl;

import com.pichincha.service.exchange.business.UserService;
import com.pichincha.service.exchange.client.ExternalServiceClient;
import com.pichincha.service.exchange.client.model.ExternalUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final ExternalServiceClient externalServiceClient;

    @Override
    public Mono<ExternalUserResponse> findUserById(Long userId, String token) {
        return externalServiceClient.getUserById(userId, token)
                .switchIfEmpty(Mono.defer(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el usuario");
                }));
    }
}
