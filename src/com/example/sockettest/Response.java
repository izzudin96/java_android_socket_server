package com.example.sockettest;

import java.io.Serializable;

public class Response implements Serializable {
    private final String message;
    private final Object object;
    private static final long serialVersionUID = 2147483649L;

    public Response(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public String getMessage()
    {
        return this.message;
    }

    public Object getObject()
    {
        return this.object;
    }
}
