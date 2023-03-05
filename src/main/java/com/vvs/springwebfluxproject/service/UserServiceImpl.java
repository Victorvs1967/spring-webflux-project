package com.vvs.springwebfluxproject.service;

import javax.xml.crypto.Data;

import org.springframework.stereotype.Service;

import com.vvs.springwebfluxproject.dto.UserDto;
import com.vvs.springwebfluxproject.mapper.DataMapper;
import com.vvs.springwebfluxproject.model.User;
import com.vvs.springwebfluxproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  
  private final UserRepository userRepository;
  private final DataMapper dataMapper;

  @Override
  public Flux<UserDto> getUsers() {
    return userRepository.findAll()
      .map(user -> dataMapper.convert(user, UserDto.class));
  }

  @Override
  public Mono<UserDto> getUser(String username) {
    return userRepository.findByUsername(username)
      .switchIfEmpty(Mono.error(new RuntimeException("User not found...")))
      .map(user -> dataMapper.convert(user, UserDto.class));
  }

  @Override
  public Mono<UserDto> updateUser(UserDto userDto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
  }

  @Override
  public Mono<UserDto> deleteUser(String username) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }
}
