package com.vvs.springwebfluxproject.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.springwebfluxproject.dto.AuthRequestDto;
import com.vvs.springwebfluxproject.dto.AuthResponseDto;
import com.vvs.springwebfluxproject.dto.UserDto;
import com.vvs.springwebfluxproject.service.AuthService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class AuthHandler {
  
  private final AuthService authService;

  public Mono<ServerResponse> login(ServerRequest request) {
    return request.bodyToMono(AuthRequestDto.class)
      .map(authService::login)
      .flatMap(token -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(token, AuthResponseDto.class));
  }

  public Mono<ServerResponse> signup(ServerRequest request) {
    return request.bodyToMono(UserDto.class)
      .map(authService::signup)
      .flatMap(user -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(user, UserDto.class));
  }
}
