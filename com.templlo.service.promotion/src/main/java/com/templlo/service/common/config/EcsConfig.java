package com.templlo.service.common.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.netflix.appinfo.EurekaInstanceConfig;

@Configuration
@Profile("prod")
public class EcsConfig {

	@Bean
	public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
		EurekaInstanceConfigBean config = new EurekaInstanceConfigBean(inetUtils);
		String ip = null;
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		config.setIpAddress(ip);
		config.setPreferIpAddress(true);
		config.setNonSecurePort(19050);

		return config;
	}

}