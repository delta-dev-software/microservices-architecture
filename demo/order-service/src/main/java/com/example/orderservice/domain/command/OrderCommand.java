package com.example.orderservice.domain.command;

import reactor.core.publisher.Mono;

public interface OrderCommand {
    Mono<Void> execute();
}

