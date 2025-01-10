package com.templlo.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableCaching
@EnableFeignClients(basePackages = "com.templlo.service.common.client")
@EnableRedisRepositories(basePackages = "com.templlo.service.coupon.repository")
public class PromotionApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromotionApplication.class, args);
	}

}
