package com.didacto.config.security;

public class AuthConstant {

    public static final String AUTH_USER = "hasRole('ROLE_USER')";
    public static final String AUTH_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String AUTH_ALL = "hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')";
    public static final String REFRESH = "hasRole('ROLE_REFRESH')";
    public static final String USER = "USER";
    public static final String ADMIN = "ADMIN";

}
