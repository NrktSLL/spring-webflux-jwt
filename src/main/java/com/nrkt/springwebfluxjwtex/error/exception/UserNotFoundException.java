package com.nrkt.springwebfluxjwtex.error.exception;

import com.nrkt.springwebfluxjwtex.error.ErrorConstants;
import com.nrkt.springwebfluxjwtex.error.exception.base.BaseRequestException;
import org.zalando.problem.Status;

public class UserNotFoundException extends BaseRequestException {
    public UserNotFoundException(String message) {
        super(ErrorConstants.DEFAULT_TYPE, "user", Status.NOT_FOUND, message);
    }
}
