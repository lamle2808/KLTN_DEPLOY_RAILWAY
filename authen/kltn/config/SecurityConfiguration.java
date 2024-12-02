package com.example.kltn.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration {
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                .csrf(csrf -> csrf
                                                .disable())
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers("/KLTN-2024/api/v1/**")
                                                .permitAll() // Permit tất cả các yêu cầu với /KLTN-2024/api/v1/**
                                                .requestMatchers("/KLTN-2024/api/v1/auth/**") // Permit cho đường dẫn auth                                                                                              // auth
                                                .permitAll()
                                                .requestMatchers("/KLTN-2024/api/manage/admin/**")
                                                .hasRole("ADMIN")
                                                .requestMatchers(AUTH_WHITELIST)
                                                .permitAll()
                                                .anyRequest()
                                                .authenticated())
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .exceptionHandling(withDefaults())
                                .authenticationProvider(authenticationProvider)
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return httpSecurity.build();
        }

        private static final String[] AUTH_WHITELIST = {
                        "/api/v1/**",
                        "/v2/api-docs",
                        "/v3/api-docs",
                        "v3/api-docs/**",
                        "api-docs.yaml",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/swagger-resources",
                        "/swagger-resources/**",
                        "/configuration/ui",
                        "/configuration/security",
                        "/webjars/**"
        };

}
