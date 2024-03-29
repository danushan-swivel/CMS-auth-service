package com.cms.auth.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cms.auth.utills.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JwtValidator extends OncePerRequestFilter {
    private final String key;

    public JwtValidator(@Value("${security.key}") String key) {
        this.key = key;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = httpServletRequest.getHeader(Constants.TOKEN_HEADER);
        if (jwtToken != null) {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(key)).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            String userName = decodedJWT.getClaim("username").toString();
            var roles = decodedJWT.getClaim("authorities").asList(String.class);
            List<SimpleGrantedAuthority> authorityList = roles.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userName, null, authorityList));
        } else {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token not available");
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] requests = {"/api/v1/user/login", "/api/v1/user/sign-up", "/v2/api-docs",
                "/swagger-resources", "/swagger-ui/", "/swagger-ui/springfox.css", "/swagger-ui/swagger-ui-bundle.js", "/swagger-ui/swagger-ui.css",
                "/swagger-resources/configuration/security", "/swagger-resources/configuration/ui", "/swagger-ui/springfox.js", "/swagger-ui/swagger-ui-standalone-preset.js",
                "/swagger-ui/favicon-32x32.png"
        };
        List<String> requestList = Arrays.asList(requests);
        return requestList.contains(request.getServletPath());
    }
}
