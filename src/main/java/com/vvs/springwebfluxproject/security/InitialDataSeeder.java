package com.vvs.springwebfluxproject.security;

import java.time.Instant;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.vvs.springwebfluxproject.model.Role;
import com.vvs.springwebfluxproject.model.User;
import com.vvs.springwebfluxproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitialDataSeeder implements ApplicationListener<ApplicationStartedEvent> {

  @Value("${admin.username}")
  private String username;

  @Value("${admin.password}")
  private String password;

  @Value("${admin.email}")
  private String email;

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void onApplicationEvent(ApplicationStartedEvent event) {
    userRepository.findByUsername(username)
      .switchIfEmpty(createAdminUser())
      .subscribe();
  }

  private Mono<User> createAdminUser() {
    User user = User.builder()
      .username(username)
      .password(passwordEncoder.encode(password))
      .email(email)
      .role(Role.ADMIN)
      .onCreate(Date.from(Instant.now()))
      .onUpdate(Date.from(Instant.now()))
      .isActive(true)
      .build();

    return userRepository.save(user)
      .doOnNext(adminUser -> log.info("Admin user created successuflly"));
  }
  
}
