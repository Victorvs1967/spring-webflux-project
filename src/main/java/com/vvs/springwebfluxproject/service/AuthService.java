package com.vvs.springwebfluxproject.service;

import com.vvs.springwebfluxproject.dto.AuthRequestDto;
import com.vvs.springwebfluxproject.dto.AuthResponseDto;
import com.vvs.springwebfluxproject.model.User;

import reactor.core.publisher.Mono;

public interface AuthService {
  public Mono<User> signup(User user);
  public Mono<AuthResponseDto> login(AuthRequestDto request);
}
