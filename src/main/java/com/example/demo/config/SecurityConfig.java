package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder;

@Configuration
public class SecurityConfig {
    private static final  String ADMIN_VAL = "admin";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/v1/account/hello").permitAll()
                                .requestMatchers("/csrf/token").permitAll()
                                .requestMatchers("/protected-resource").hasAuthority(ADMIN_VAL)
                                .requestMatchers("/api/v1/account/balance").authenticated()


                )
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        var user = withDefaultPasswordEncoder()
                .username("user")
                .password("12345")
                .authorities("read")
                .build();
        var admin = withDefaultPasswordEncoder()
                .username(ADMIN_VAL)
                .password("12345")
                .authorities(ADMIN_VAL)
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}