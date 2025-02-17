package com.pichincha.service.exchange.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pichincha.service.exchange.config.WebClientConfig;
import com.pichincha.service.exchange.client.model.ExternalExchangeResponse;
import com.pichincha.service.exchange.client.model.ExternalUserResponse;
import com.pichincha.service.expose.dto.request.ExchangeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ExternalServiceClient {

    private final WebClientConfig webClientConfig;

    public Mono<ExternalUserResponse> getUserById(Long id, String token) {
        return webClientConfig.gorestWebClientBuilder()
                .get()
                .uri("/public/v2/users/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> {
                            ObjectMapper objectMapper = new ObjectMapper();

                            JsonNode rootNode = null;
                            try {
                                rootNode = objectMapper.readTree(errorBody);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }

                            return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, rootNode.path("message").asText()));
                        }))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno"))))
                .bodyToMono(ExternalUserResponse.class);
    }


    public Mono<Void> createExchange(ExchangeRequest externalExchangeRequest) {
        return webClientConfig.apiSupportWebClientBuilder()
                .post()
                .uri("/exchange")
                .bodyValue(externalExchangeRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorBody))))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(Void.class);
    }

    public Mono<Void> updateExchange(Long id, ExchangeRequest externalExchangeUpdateRequest) {
        return webClientConfig.apiSupportWebClientBuilder()
                .patch()
                .uri("/exchange/{id}", id)
                .bodyValue(externalExchangeUpdateRequest)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorBody))))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(Void.class);
    }

    public Mono<ExternalExchangeResponse> getExchangeById(Long id) {
        return webClientConfig.apiSupportWebClientBuilder()
                .get()
                .uri("/exchange/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorBody))))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToMono(ExternalExchangeResponse.class);
    }

    public Flux<ExternalExchangeResponse> listExchangeByUserId(Long userId) {
        return webClientConfig.apiSupportWebClientBuilder()
                .get()
                .uri(uriBuilder -> uriBuilder.path("/exchange/user")
                        .queryParam("userId", userId)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errorBody))))
                .onStatus(HttpStatusCode::is5xxServerError, response -> response.bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, errorBody))))
                .bodyToFlux(ExternalExchangeResponse.class);
    }

}
