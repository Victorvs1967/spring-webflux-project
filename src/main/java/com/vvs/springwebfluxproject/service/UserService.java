package com.vvs.springwebfluxproject.service;

import com.vvs.springwebfluxproject.dto.UserDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
  Flux<UserDto> getUsers();
  Mono<UserDto> getUser(String username);
  Mono<UserDto> updateUser(UserDto userDto);
  Mono<UserDto> deleteUser(String username);
}
