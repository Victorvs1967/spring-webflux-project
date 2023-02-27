package com.vvs.springwebfluxproject.router;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.springwebfluxproject.handler.AuthHandler;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class AuthRouter {
  
  public RouterFunction<ServerResponse> authRouterFunction(AuthHandler handler) {
    return route()
      .nest(path("/auth"), builder -> builder
        .POST("/login", handler::login)
        .POST("/signup", handler::signup))
      .build();
  }
}
