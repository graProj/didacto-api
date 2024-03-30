package com.didacto.config.security;

public class AuthConstant {

    public static final String AUTH_USER = "hasRole('ROLE_USER')";
    public static final String AUTH_ADMIN = "hasRole('ROLE_ADMIN')";
    public static final String AUTH_ALL = "hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')";

    public static final String AUTH_REFRESH = "hasRole('ROLE_REFRESH')";

}
