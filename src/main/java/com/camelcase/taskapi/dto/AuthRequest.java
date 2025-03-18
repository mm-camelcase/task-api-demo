package com.camelcase.taskapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates getters, setters, toString, equals, and hashCode
@AllArgsConstructor  // Generates an all-args constructor
@NoArgsConstructor  // Generates a no-args constructor
public class AuthRequest {
    private String username;
    private String password;
}
