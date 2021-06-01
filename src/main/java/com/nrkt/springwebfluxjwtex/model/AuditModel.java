package com.nrkt.springwebfluxjwtex.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.*;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AuditModel {

    @CreatedDate
    LocalDateTime creationDate;

    @LastModifiedDate
    LocalDateTime lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    String updatedBy;

    @Version
    Long version;
}