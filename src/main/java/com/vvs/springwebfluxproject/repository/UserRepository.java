package com.vvs.springwebfluxproject.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.vvs.springwebfluxproject.model.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
  Mono<User> findByUsername(String username);
}
