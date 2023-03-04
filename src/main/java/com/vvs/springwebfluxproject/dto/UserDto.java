package com.vvs.springwebfluxproject.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vvs.springwebfluxproject.model.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

  private String id;
  private String username;
  private String password;
  private Role role;

  private String firstName;
  private String lastName;
  private String phone;
  private String address;

  private Date onCreate;
  private Date onUpdate;

}
