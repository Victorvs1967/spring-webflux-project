package com.vvs.springwebfluxproject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vvs.springwebfluxproject.dto.AuthRequestDto;
import com.vvs.springwebfluxproject.dto.AuthResponseDto;
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

  @Override
  public Mono<User> signup(User user) {
    return userRepository.findByUsername(user.getUsername())
      .filter(userExist -> userExist != null)
      .switchIfEmpty(Mono.error(new RuntimeException("User already exist...")))
      .map(usr -> user)
      .flatMap(userRepository::save);
  }

  @Override
  public Mono<AuthResponseDto> login(AuthRequestDto request) {
    return userRepository.findByUsername(request.getUsername())
      .filter(userDetails -> passwordEncoder.matches(request.getPassword(), userDetails.getPassword()))
      .map(userDetails -> jwtUtil.generateToke(userDetails))
      .map(token -> AuthResponseDto.builder()
        .response(token)
        .build());
  }
  
}
