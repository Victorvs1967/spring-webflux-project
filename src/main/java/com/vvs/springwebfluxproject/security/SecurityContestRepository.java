package com.vvs.springwebfluxproject.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityContestRepository implements ServerSecurityContextRepository {
  
  private final AuthenticationManager authenticationManager;

  private final static String AUTH_TOKEN_PREFIX = "Bearer ";
  
  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {
    return Mono.just(exchange.getRequest())
      .map(request -> request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
      .filter(token -> token != null && token.startsWith(AUTH_TOKEN_PREFIX))
      .map(token -> token.substring(AUTH_TOKEN_PREFIX.length()))
      .map(authToken -> new UsernamePasswordAuthenticationToken(authToken, authToken))
      .flatMap(authenticationManager::authenticate)
      .switchIfEmpty(Mono.error(new RuntimeException("No authorized...")))
      .map(SecurityContextImpl::new);
  }
  
    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
      throw new UnsupportedOperationException("Unimplemented method 'save'");
    }
}
