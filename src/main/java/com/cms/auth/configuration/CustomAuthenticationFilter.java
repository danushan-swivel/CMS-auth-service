package com.cms.auth.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cms.auth.utills.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final String key;
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, @Value("${security.key}") String key) {
        this.authenticationManager = authenticationManager;
        this.key = key;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String jwtToken = JWT.create().withIssuer("Danushan")
                .withSubject("JWT TOKEN")
                .withClaim("username", user.getUsername())
                .withClaim("authorities", authoritiesList(authentication.getAuthorities()))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(new Date(System.currentTimeMillis()).getTime() + 100 * 60 * 1000))
                .sign(Algorithm.HMAC256(key));
        response.setHeader(Constants.TOKEN_HEADER, jwtToken);
        chain.doFilter(request, response);
    }

    private List<String> authoritiesList(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritySet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritySet.add(authority.getAuthority());
        }
        return new ArrayList<>(authoritySet);
    }
}
