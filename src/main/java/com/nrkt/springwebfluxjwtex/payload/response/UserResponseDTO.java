package com.nrkt.springwebfluxjwtex.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(value = JsonInclude.Include.NON_NULL, valueFilter = UserResponseDTO.class)
public class UserResponseDTO implements Serializable {
    @JsonProperty("id")
    @Schema(name = "id", required = true)
    String id;

    @JsonProperty("username")
    @Schema(name = "username", example = "Foo", required = true)
    @NotBlank
    String username;

    @JsonProperty("email")
    @Schema(name = "email", example = "aa@gmail.com", required = true)
    @Email
    @NotBlank
    String email;
}
