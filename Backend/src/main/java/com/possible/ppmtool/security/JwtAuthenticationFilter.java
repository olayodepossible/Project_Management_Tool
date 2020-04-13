package com.possible.ppmtool.security;

import com.possible.ppmtool.model.User;
import com.possible.ppmtool.services.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static com.possible.ppmtool.security.SecurityConstants.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromHttpRequest(httpServletRequest);
            if(StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)){
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);
                User userDetails = customUserDetailService.loadUserById(userId);
                UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.emptyList()
                );

                userAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            }

        }catch (Exception ex){
            logger.error("Could not set user authentication in security context ", ex);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


    private String getJwtFromHttpRequest(HttpServletRequest request){
        String bearerToken = request.getHeader(HEADER_STRING);

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)){
            return bearerToken.substring(7, bearerToken.length());
        }
        return  null;
    }
}
