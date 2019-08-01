package com.example.pokemeet;

import java.io.Serializable;

public class Request implements Serializable {
    private final String email;
    private final String action;
    private final String[] parameter;
    private static final long serialVersionUID = 2147483649L;

    public Request(String email, String action, String... parameter) {
        this.email = email;
        this.action = action;
        this.parameter = parameter;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getAction()
    {
        return this.action;
    }

    public String[] getParameter()
    {
        return this.parameter;
    }
}
