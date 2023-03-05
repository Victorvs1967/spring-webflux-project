package com.vvs.springwebfluxproject.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.springwebfluxproject.dto.UserDto;
import com.vvs.springwebfluxproject.security.JwtUtil;
import com.vvs.springwebfluxproject.service.UserService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class UserHandler {
  
  private final UserService userService;
  private final JwtUtil jwtUtil;

  public Mono<ServerResponse> getUsers(ServerRequest request) {
    String token = request.headers().firstHeader("authorization").substring(7);
    return jwtUtil.validateToken(token)
      .map(result -> !result)
      .switchIfEmpty(Mono.error(new RuntimeException("Not authorization...")))
      .flatMap(credentials -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(userService.getUsers(), UserDto.class));
  }
}
