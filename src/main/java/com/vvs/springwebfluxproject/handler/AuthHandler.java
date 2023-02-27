package com.vvs.springwebfluxproject.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.vvs.springwebfluxproject.dto.AuthResponseDto;
import com.vvs.springwebfluxproject.service.AuthService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class AuthHandler {
  
  private final AuthService authService;

  public Mono<ServerResponse> login(ServerRequest request) {
    return ServerResponse
      .ok()
      .contentType(APPLICATION_JSON)
      .body("", AuthResponseDto.class);
  }
}
