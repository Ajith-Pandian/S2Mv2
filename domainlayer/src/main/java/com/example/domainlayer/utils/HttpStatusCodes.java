package com.example.domainlayer.utils;

/**
 * Created by thoughtchimp on 12/1/2016.
 */

public class HttpStatusCodes {
    //Success codes
    public final static int OK = 200;
    public final static int CREATED = 201;
    public final static int NO_CONTENT = 204;

    //Redirection
    public final static int NOT_MODIFIED = 304;

    //Error codes
    public final static int BAD_REQUEST = 400;
    public final static int UNAUTHORIZED = 401;
    public final static int FORBIDDEN = 403;
    public final static int NOT_FOUND = 404;
    public final static int CONFLICT = 409;
    public final static int TIMEOUT = 504;
}
