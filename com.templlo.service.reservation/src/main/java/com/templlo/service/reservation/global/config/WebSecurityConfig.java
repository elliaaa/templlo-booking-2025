package com.templlo.service.reservation.global.config;

import com.templlo.service.reservation.global.common.exception.GlobalFilterExceptionHandlerFilter;
import com.templlo.service.reservation.global.security.CustomAuthenticationFilter;
import com.templlo.service.reservation.global.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final CustomAuthenticationFilter customAuthenticationFilter;
    private final GlobalFilterExceptionHandlerFilter globalFilterExceptionHandlerFilter;

    private final String ROLE_MEMBER = UserRole.MEMBER.name();
    private final String ROLE_TEMPLE = UserRole.TEMPLE_ADMIN.name();
    private final String ROLE_MASTER = UserRole.MASTER.name();

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                .authorizeHttpRequests((request) -> request

                        .requestMatchers("/api/test/kafka/**").permitAll()
                        .requestMatchers("/api/test/reservation-exception").permitAll()
                        .requestMatchers("/api/test/auth/user").hasAnyAuthority(ROLE_MEMBER, ROLE_MASTER)
                        .requestMatchers("/api/test/auth/temple").hasAnyAuthority(ROLE_TEMPLE, ROLE_MASTER)
                        .requestMatchers("/api/checker/**").permitAll()
                        .anyRequest().authenticated())
                ;

        // filter 순서 : 안쪽부터 등록해야 함. 필터 순서는 바깥쪽부터 GlobalFilterExceptionHandlerFilter > CustomAuthenticationFilter > UsernamePasswordAuthenticationFilter
        http
                .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(globalFilterExceptionHandlerFilter, CustomAuthenticationFilter.class)
        ;

        http
                .formLogin(AbstractHttpConfigurer::disable)
                ;

        return http.build();
    }
}
