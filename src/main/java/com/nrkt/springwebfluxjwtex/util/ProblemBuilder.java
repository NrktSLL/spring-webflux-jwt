package com.nrkt.springwebfluxjwtex.util;

import lombok.experimental.UtilityClass;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

@UtilityClass
public class ProblemBuilder {
    public Problem createProblem(String message, Exception exception, Status httpStatus) {
        return Problem.builder()
                .withTitle(message)
                .withDetail(exception.getMessage())
                .withStatus(httpStatus)
                .withCause((ThrowableProblem) exception.getCause())
                .build();
    }

    public Problem createProblem(String message, String detail, Exception exception, Status httpStatus) {
        return Problem.builder()
                .withTitle(message)
                .withDetail(detail)
                .withStatus(httpStatus)
                .withCause((ThrowableProblem) exception.getCause())
                .build();
    }
}
