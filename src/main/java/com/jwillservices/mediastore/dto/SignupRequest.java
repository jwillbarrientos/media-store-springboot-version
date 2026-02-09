package com.jwillservices.mediastore.dto;

import lombok.Getter;
import lombok.Setter;

public class SignupRequest {
    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;
}
