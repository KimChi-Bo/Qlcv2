package com.example.qlycv.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.qlycv.auth.CustomUserDetails;
import com.example.qlycv.constant.Constant;
import com.example.qlycv.service.JwtService;
import com.example.qlycv.service.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {
	
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = (String) request.getSession().getAttribute(Constant.JWT);

            if (StringUtils.hasText(token) && jwtService.validateToken(token)) {
                // get username
                String userName = jwtService.getUserNameFromJWT(token);
                // get UserDetails by username
                UserDetails userDetails = userService.loadUserByUsername(userName);
                if (!ObjectUtils.isEmpty(userDetails)) {
                    // set data for Security Context
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            throw ex;
        }

        filterChain.doFilter(request, response);
    }
}
