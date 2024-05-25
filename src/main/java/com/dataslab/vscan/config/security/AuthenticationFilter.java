package com.dataslab.vscan.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends GenericFilterBean {


    private final AuthenticationService authenticationService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        var httpServletRequest = (HttpServletRequest) request;
        if(AntPathRequestMatcher.antMatcher("/api/**").matches(httpServletRequest)) {
            try {
                Authentication authentication = authenticationService.getAuthentication(httpServletRequest);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception exp) {
                handleException((HttpServletResponse) response, exp);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleException(HttpServletResponse httpResponse, Exception exception) throws IOException {

        try(PrintWriter writer = httpResponse.getWriter()) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

            writer.print(exception.getMessage());
            writer.flush();
        }
    }
}
