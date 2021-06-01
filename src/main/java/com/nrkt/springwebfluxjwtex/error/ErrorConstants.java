package com.nrkt.springwebfluxjwtex.error;

import java.net.URI;

public final class ErrorConstants {

    public static final URI DEFAULT_TYPE = URI.create("problem-with-message");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create("email-already-used");

    private ErrorConstants() { }
}
