package com.srinjay.book_network.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class BeansConfig {
    @Bean
    public AuthenticationProvider authenticationProvider() {

    }
}
