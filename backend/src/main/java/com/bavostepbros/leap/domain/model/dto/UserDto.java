package com.bavostepbros.leap.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Integer userId;
    Integer roleId;
    String username;
    String email;
    String password;
}
