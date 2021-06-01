package com.nrkt.springwebfluxjwtex.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LoginDTO implements Serializable {
    @JsonProperty("email")
    @Schema(name = "email", example = "aa@gmail.com", required = true)
    @NotBlank
    @Email
    String email;

    @JsonProperty("password")
    @Schema(name = "password", example = "abc12345", required = true)
    @NotBlank
    String password;
}
