package com.camelcase.taskapi.dto;

// import lombok.AllArgsConstructor;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data  // Generates getters, setters, toString, equals, and hashCode
// @AllArgsConstructor  // Generates an all-args constructor
// @NoArgsConstructor  // Generates a no-args constructor
// public class AuthRequest {
//     private String username;
//     private String password;
// }




public class AuthRequest {
    private String username;
    private String password;

    // No-args constructor (needed for JSON deserialization)
    public AuthRequest() {
    }

    // All-args constructor
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "AuthRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

