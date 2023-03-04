package com.vvs.springwebfluxproject.service;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vvs.springwebfluxproject.dto.AuthRequestDto;
import com.vvs.springwebfluxproject.dto.AuthResponseDto;
import com.vvs.springwebfluxproject.dto.UserDto;
import com.vvs.springwebfluxproject.mapper.DataMapper;
import com.vvs.springwebfluxproject.model.Role;
import com.vvs.springwebfluxproject.model.User;
import com.vvs.springwebfluxproject.repository.UserRepository;
import com.vvs.springwebfluxproject.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final JwtUtil jwtUtil;
  private final DataMapper dataMapper;

  @Override
  public Mono<UserDto> signup(UserDto userDto) {
    return isUserExist(userDto.getUsername())
      .filter(isExist -> !isExist)
      .switchIfEmpty(Mono.error(new RuntimeException("User already exist...")))
      .map(isUser -> userDto)
      .map(user -> dataMapper.convert(user, User.class))
      .doOnNext(user -> {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(user.getRole() != null ? user.getRole() : Role.USER);
        user.setOnCreate(Date.from(Instant.now()));
        user.setOnUpdate(Date.from(Instant.now()));
      })
      .flatMap(userRepository::save)
      .map(user -> dataMapper.convert(user, UserDto.class));
  }

  @Override
  public Mono<AuthResponseDto> login(AuthRequestDto request) {
    return userRepository.findByUsername(request.getUsername())
      .filter(userDetails -> passwordEncoder.matches(request.getPassword(), userDetails.getPassword()))
      .switchIfEmpty(Mono.error(new RuntimeException("User not found...")))
      .map(userDetails -> jwtUtil.generateToke(userDetails))
      .map(token -> AuthResponseDto.builder()
        .token(token)
        .build());
  }

  private Mono<Boolean> isUserExist(String username) {
    return userRepository.findByUsername(username)
      .map(user -> true)
      .switchIfEmpty(Mono.just(false));
  }
  
}
