package com.management.project_collaboration_api.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    try {
        String jwt = parseJwt(request);
        if (jwt != null && jwtUtils.validateToken(jwt)) {
            String email = jwtUtils.getEmailFromToken(jwt);
            
            // 1. Extract the role string from the token
            String role = jwtUtils.getRoleFromToken(jwt); 

            // 2. Wrap it in a GrantedAuthority object. 
            // NOTE: Spring's hasRole() looks for the "ROLE_" prefix automatically.
            List<SimpleGrantedAuthority> authorities = java.util.Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + role)
            );

            // 3. Pass the authorities list as the third parameter
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email, 
                    null, 
                    authorities); // <--- Changed from null to authorities

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    } catch (Exception e) {
        logger.error("Cannot set user authentication: {}", e);
    }
    filterChain.doFilter(request, response);
}

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}