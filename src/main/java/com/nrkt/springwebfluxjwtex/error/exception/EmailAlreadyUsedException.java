package com.nrkt.springwebfluxjwtex.error.exception;

import com.nrkt.springwebfluxjwtex.error.ErrorConstants;
import com.nrkt.springwebfluxjwtex.error.exception.base.BaseRequestException;
import org.zalando.problem.Status;

public class EmailAlreadyUsedException extends BaseRequestException {
    public EmailAlreadyUsedException(String message) {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "email", Status.CONFLICT, message);
    }
}
