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
public class UserRequestDTO implements Serializable {
    @JsonProperty("name")
    @NotBlank
    @Schema(name = "name", example = "Foo", required = true)
    String name;

    @Schema(name = "email", example = "aa@gmail.com", required = true)
    @JsonProperty("email")
    @Email
    @NotBlank
    String email;

    @Schema(name = "password", example = "abc12345", required = true)
    @JsonProperty("password")
    @ToString.Exclude
    @NotBlank
    String password;
}
