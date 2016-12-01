package com.example.uilayer.customUtils;

/**
 * Created by thoughtchimp on 12/1/2016.
 */

public class HttpStatusCodeHandler {
    //Success codes
    private final static int OK = 200;
    private final static int CREATED = 201;
    private final static int NO_CONTENT = 204;

    //Redirection
    private final static int NOT_MODIFIED = 304;

    //Error codes
    private final static int BAD_REQUEST = 400;
    private final static int UNAUTHORIZED = 401;
    private final static int FORBIDDEN = 403;
    private final static int NOT_FOUND = 404;
    private final static int CONFLICT = 409;
}
