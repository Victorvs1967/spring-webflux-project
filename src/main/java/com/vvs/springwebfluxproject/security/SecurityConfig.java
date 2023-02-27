package com.vvs.springwebfluxproject.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationManager authenticationManager;
  private final SecurityContestRepository securityContestRepository;

  private final static String[] WHITE_LIST_URLS = {"/**", "/auth/signap", "/auth/login"};

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
      .csrf().disable()
      .formLogin().disable()
      .exceptionHandling()
      .authenticationEntryPoint((shs, e) -> Mono.fromRunnable(() -> shs.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
      .accessDeniedHandler((shs, e) -> Mono.fromRunnable(() -> shs.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))
      .and()
      .authenticationManager(authenticationManager)
      .securityContextRepository(securityContestRepository)
      .authorizeExchange()
      .pathMatchers(HttpMethod.OPTIONS).permitAll()
      .pathMatchers(WHITE_LIST_URLS).permitAll()
      .anyExchange().authenticated()
      .and()
      .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
