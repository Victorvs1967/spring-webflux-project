package com.vvs.springwebfluxproject.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.vvs.springwebfluxproject.model.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
  Mono<User> findByUsername(String username);
  Mono<User> findByEmail(String email);
}
