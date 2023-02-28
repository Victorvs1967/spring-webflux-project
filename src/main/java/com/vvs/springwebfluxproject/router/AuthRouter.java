package com.vvs.springwebfluxproject.router;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.springwebfluxproject.handler.AuthHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class AuthRouter {
  
  @Bean
  public RouterFunction<ServerResponse> authRouterFunction(AuthHandler handler) {
    return route()
      .nest(path("/auth"), builder -> builder
        .POST("/login", handler::login)
        .POST("/signup", handler::signup))
      .build();
  }
}
