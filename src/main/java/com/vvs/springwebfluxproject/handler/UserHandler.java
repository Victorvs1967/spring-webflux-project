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
    return jwtUtil.validateToken(getToken(request))
      .map(result -> !result)
      .switchIfEmpty(Mono.error(new RuntimeException("Not authorization...")))
      .flatMap(credentials -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(userService.getUsers(), UserDto.class));
  }

  public Mono<ServerResponse> getUser(ServerRequest request) {
    return jwtUtil.validateToken(getToken(request))
      .map(result -> !result)
      .switchIfEmpty(Mono.error(new RuntimeException("Not authorization...")))
      .map(isUsername -> request.pathVariable("username"))
      .map(userService::getUser)
      .flatMap(user -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(user, UserDto.class));
  }

  public Mono<ServerResponse> updateUser(ServerRequest request) {
    return jwtUtil.validateToken(getToken(request))
      .map(result -> !result)
      .switchIfEmpty(Mono.error(new RuntimeException("Not authorization...")))
      .map(isUsername -> request.pathVariable("username"))
      .map(userService::getUser)
      .switchIfEmpty(Mono.error(new RuntimeException("User not found...")))
      .flatMap(credantial -> request.bodyToMono(UserDto.class))
      .map(userService::updateUser)
      .flatMap(user -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(user, UserDto.class));
  }

  public Mono<ServerResponse> deleteUser(ServerRequest request) {
    return jwtUtil.validateToken(getToken(request))
      .map(result -> !result)
      .switchIfEmpty(Mono.error(new RuntimeException("Not authorization...")))
      .map(isUsername -> request.pathVariable("username"))
      .flatMap(username -> ServerResponse
        .ok()
        .contentType(APPLICATION_JSON)
        .body(userService.deleteUser(username), UserDto.class));
  }

  private String getToken(ServerRequest request) {
    String header = request.headers().firstHeader("authorization");
    return header != null ?  header.substring(7) : (new RuntimeException("Headers error")).getMessage();
  }
}
