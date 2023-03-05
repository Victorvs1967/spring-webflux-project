package com.vvs.springwebfluxproject.model;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Document("users")
public class User implements UserDetails {

  @Id
  private String id;
  private String username;
  private String password;
  private String email;
  private Role role;

  private String firstName;
  private String lastName;
  private String phone;
  private String address;

  private Date onCreate;
  private Date onUpdate;
  private Boolean isActive;

  public User() {
    role = Role.USER;
    onCreate = Date.from(Instant.now());
    onUpdate = onCreate;
    isActive = true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.name()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return isActive;
  }

  @Override
  public boolean isAccountNonLocked() {
    return isActive;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return isActive;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
  
}
