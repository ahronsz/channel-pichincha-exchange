package com.pichincha.service.expose.web;

import com.pichincha.service.exchange.business.ExchangeService;
import com.pichincha.service.expose.dto.request.ExchangeRequest;
import com.pichincha.service.expose.dto.response.ExchangeResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @PostMapping
    public Mono<ResponseEntity<Void>> createExchange(
            @Valid @RequestBody ExchangeRequest exchangeRequest,
            @RequestHeader("Authorization") String token) {
        return exchangeService.createExchange(exchangeRequest, token)
                .then(Mono.just(ResponseEntity.status(HttpStatus.CREATED).build()));
    }

    @PatchMapping("/{id}")
    public Mono<ResponseEntity<Void>> updateExchange(
            @PathVariable("id") @NotNull @Positive Long id,
            @Valid @RequestBody ExchangeRequest exchangeUpdateRequest,
            @RequestHeader("Authorization") String token) {
        return exchangeService.updateExchange(id, exchangeUpdateRequest, token)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ExchangeResponse>> findExchange(
            @PathVariable("id") @NotNull @Positive Long id,
            @RequestParam("userId") @NotNull @Positive Long userId,
            @RequestHeader("Authorization") String token) {
        return exchangeService.getExchangeById(id, userId, token)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/user")
    public ResponseEntity<Flux<ExchangeResponse>> list(
            @RequestParam("userId") @NotNull @Positive Long userId,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(exchangeService.listExchangeByUserId(userId, token));
    }
}
