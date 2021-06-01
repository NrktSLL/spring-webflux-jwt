package com.nrkt.springwebfluxjwtex.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL, valueFilter = LoginResponseDTO.class)
public class LoginResponseDTO implements Serializable {
    @JsonProperty("token")
    String token;
}
