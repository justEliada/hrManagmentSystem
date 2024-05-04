package com.example.backend.dto.userDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String password;
}
