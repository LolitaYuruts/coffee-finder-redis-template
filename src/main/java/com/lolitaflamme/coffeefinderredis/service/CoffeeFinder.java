package com.lolitaflamme.coffeefinderredis.service;

import com.lolitaflamme.coffeefinderredis.domain.Coffee;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class CoffeeFinder {

    @NonNull
    private final RedisConnectionFactory redisConnectionFactory;
    @NonNull
    private final RedisOperations redisOperations;

    private final WebClient client = WebClient.create("http://localhost:8085/coffees");

    @Scheduled(fixedRate = 1000)
    private void findCoffees() {
        redisConnectionFactory.getConnection().serverCommands().flushDb();

        client.get()
                .retrieve()
                .bodyToFlux(Coffee.class)
                .toStream()
                .forEach(c -> redisOperations.opsForValue().set(c.getId(), c));

        redisOperations.opsForValue()
                .getOperations()
                .keys("*")
                .forEach(c -> System.out.println(redisOperations.opsForValue().get(c)));
    }
}
