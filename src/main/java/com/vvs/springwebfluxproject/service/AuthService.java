package com.vvs.springwebfluxproject.service;

import com.vvs.springwebfluxproject.dto.AuthRequestDto;
import com.vvs.springwebfluxproject.dto.AuthResponseDto;
import com.vvs.springwebfluxproject.dto.UserDto;

import reactor.core.publisher.Mono;

public interface AuthService {
  public Mono<UserDto> signup(UserDto user);
  public Mono<AuthResponseDto> login(AuthRequestDto request);
}
