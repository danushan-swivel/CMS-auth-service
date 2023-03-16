package com.cms.auth.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cms.auth.utills.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = httpServletRequest.getHeader(Constants.TOKEN_HEADER);
        if (jwtToken != null) {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Constants.SECURITY_KEY.getBytes())).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            String userName = decodedJWT.getClaim("username").toString();
            var roles = decodedJWT.getClaim("authorities").asList(String.class);
            List<SimpleGrantedAuthority> authorityList = roles.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(userName, null, authorityList));
        } else {
            throw new BadCredentialsException("Invalid JWT token");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/api/v1/user/sign-up") || request.getServletPath().equals("/api/v1/user/login")
                || request.getServletPath().equals("/api/v1/user/error");
    }
}
