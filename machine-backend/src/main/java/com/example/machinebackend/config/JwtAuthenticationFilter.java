package com.example.machinebackend.config;

import com.example.machinebackend.utils.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 【诊断日志 1】: 打印当前请求的URI
        logger.info("Processing request for URI: {}", request.getRequestURI());

        String jwt = getJwtFromRequest(request);

        try {
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String username = tokenProvider.getUsernameFromJWT(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 【诊断日志 2】: 打印即将设置到 SecurityContext 的认证信息
                logger.info("Authentication successful. Setting SecurityContext for user: {}, with roles: {}", userDetails.getUsername(), userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else if (StringUtils.hasText(jwt)) {
                logger.warn("JWT token validation failed for: {}. TokenProvider returned false.", jwt);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context.", ex); // 【修改点】打印完整的异常
        }

        // 【诊断日志 3】: 打印即将执行过滤器链之前的 SecurityContext 状态
        logger.info("Before calling filterChain.doFilter for URI: {}. Authentication object is: {}", request.getRequestURI(), SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);

        // 【诊断日志 4】: 打印执行完过滤器链之后的 SecurityContext 状态
        logger.info("After calling filterChain.doFilter for URI: {}. Authentication object is: {}", request.getRequestURI(), SecurityContextHolder.getContext().getAuthentication());
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 