package com.nrkt.springwebfluxjwtex.error.exception.base;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;

import java.net.URI;

public class BaseRequestException extends AbstractThrowableProblem {
    public BaseRequestException(URI uri, String title, StatusType status, String detail) {
        super(uri, title, status, detail);
    }
}
