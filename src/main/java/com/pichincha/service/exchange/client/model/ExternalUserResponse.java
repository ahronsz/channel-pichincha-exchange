package com.pichincha.service.exchange.client.model;

public record ExternalUserResponse(
    Integer id,
    String name,
    String email,
    String gender,
    String status
) {
}
