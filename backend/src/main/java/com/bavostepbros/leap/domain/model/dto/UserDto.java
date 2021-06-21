package com.bavostepbros.leap.domain.model.dto;

import java.util.Set;

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
    String username;
    String email;
    Set<RoleDto> roleDto;
}
