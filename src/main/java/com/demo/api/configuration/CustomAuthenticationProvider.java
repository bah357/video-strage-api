package com.demo.api.configuration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final String username;
    private final String password;
    private final List<String> roles;

    public CustomAuthenticationProvider(BasicAuthorizationProperties basicAuthorizationProperties) {
        username = basicAuthorizationProperties.getName();
        password = basicAuthorizationProperties.getPassword();
        roles = basicAuthorizationProperties.getRoles();
    }

    @Override
    public Authentication authenticate(Authentication auth) {
        final var inputUsername = auth.getName();
        final var inputPassword = auth.getCredentials().toString();

        if (username.equals(inputUsername) && password.equals(inputPassword)) {
            return new UsernamePasswordAuthenticationToken(inputUsername, inputPassword,
                    Stream.ofNullable(roles)
                            .flatMap(Collection::stream)
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()));
        } else {
            throw new BadCredentialsException("External system authentication failed");
        }
    }

    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}