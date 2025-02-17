package com.pichincha.service.exchange.business;

import com.pichincha.service.exchange.client.model.ExternalUserResponse;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<ExternalUserResponse> findUserById(Long userId, String token);
}
