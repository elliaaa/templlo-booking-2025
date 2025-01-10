package com.templlo.service.temple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TempleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TempleApplication.class, args);
	}

}
