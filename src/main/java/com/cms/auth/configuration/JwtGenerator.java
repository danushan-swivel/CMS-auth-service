package com.cms.auth.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cms.auth.utills.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class JwtGenerator extends OncePerRequestFilter {

    private final String key;

    public JwtGenerator(@Value("${security.key}") String key) {
        this.key = key;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();
        if (authentication != null) {
            String jwtToken = JWT.create().withIssuer("Danushan")
                    .withSubject("JWT TOKEN")
                    .withClaim("username", authentication.getName())
                    .withClaim("authorities", authoritiesList(authentication.getAuthorities()))
                    .withIssuedAt(new Date(System.currentTimeMillis()))
                    .withExpiresAt(new Date(new Date(System.currentTimeMillis()).getTime() + 2000 * 60  * 1000))
                    .sign(Algorithm.HMAC256(key));
            httpServletResponse.setHeader(Constants.TOKEN_HEADER, jwtToken);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private List<String> authoritiesList(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritySet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritySet.add(authority.getAuthority());
        }
        return new ArrayList<>(authoritySet);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/api/v1/user/users") || request.getServletPath().equals("/v3/api-docs/**")
                || request.getServletPath().equals("/swagger-ui.html") || request.getServletPath().equals("/swagger-ui/**");
    }
}
