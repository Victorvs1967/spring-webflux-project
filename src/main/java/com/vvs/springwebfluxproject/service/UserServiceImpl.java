package com.vvs.springwebfluxproject.service;

import java.time.Instant;
import java.util.Date;

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
    return userRepository.findByUsername(userDto.getUsername())
      .switchIfEmpty(Mono.error(new RuntimeException("User not found...")))
      .map(user -> User.builder()
        .id(user.getId())
        .username(user.getUsername())
        .password(user.getPassword())
        .email(user.getEmail())
        .firstName(userDto.getFirstName())
        .lastName(userDto.getLastName())
        .phone(userDto.getPhone())
        .address(userDto.getAddress())
        .onCreate(user.getOnCreate())
        .onUpdate(Date.from(Instant.now()))
        .isActive(user.getIsActive())
        .role(userDto.getRole())
        .build())
      .flatMap(userRepository::save)
      .map(user -> dataMapper.convert(user, UserDto.class));
  }

  @Override
  public Mono<UserDto> deleteUser(String username) {
    return userRepository.findByUsername(username)
        .switchIfEmpty(Mono.error(new RuntimeException("User not found...")))
        .flatMap(this::delete).log()
        .map(user -> dataMapper.convert(user, UserDto.class));
  }

  private Mono<User> delete(User user) {
    return Mono.fromSupplier(() -> {
      userRepository
        .delete(user)
        .subscribe();
      return user;
    });
  }

}
