package com.nrkt.springwebfluxjwtex.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
class ErrorDetail implements Serializable {
    @JsonProperty("type")
    String type;
    @JsonProperty("title")
    String title;
    @JsonProperty("status")
    String status;
    @JsonProperty("detail")
    String detail;
}
