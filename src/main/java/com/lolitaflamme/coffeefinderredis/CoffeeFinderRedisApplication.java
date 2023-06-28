package com.lolitaflamme.coffeefinderredis;

import com.lolitaflamme.coffeefinderredis.domain.Coffee;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class CoffeeFinderRedisApplication {

	@Bean
	public RedisOperations<String, Coffee> redisOperations(RedisConnectionFactory factory) {
		Jackson2JsonRedisSerializer<Coffee> serializer = new Jackson2JsonRedisSerializer<>(Coffee.class);

		RedisTemplate<String, Coffee> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.setDefaultSerializer(serializer);
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		return  redisTemplate;
	}

	public static void main(String[] args) {
		SpringApplication.run(CoffeeFinderRedisApplication.class, args);
	}

}
