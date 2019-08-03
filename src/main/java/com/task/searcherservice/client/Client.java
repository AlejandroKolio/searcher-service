package com.task.searcherservice.client;

import lombok.NonNull;
import reactor.core.publisher.Mono;

/**
 * @author Alexander Shakhov
 */
public interface Client {

    Mono<String> getData(@NonNull String input);

}
