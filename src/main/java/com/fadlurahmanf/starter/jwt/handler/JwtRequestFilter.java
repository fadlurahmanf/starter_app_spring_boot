package com.fadlurahmanf.starter.jwt.handler;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomIOException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.identity.constant.IdentityURL;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    JWTTokenUtil jwtTokenUtil;

    @Autowired
    JWTUserDetailService jwtUserDetailService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().contains("/debug/") ||
                checkIsNotFiltering(request, IdentityURL.basePrefix, IdentityURL.pathRegister) ||
                checkIsNotFiltering(request, IdentityURL.basePrefix, IdentityURL.pathLogin) ||
                checkIsNotFiltering(request, IdentityURL.basePrefix, IdentityURL.pathRefreshToken);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, CustomIOException, IOException {
        try {
            logger.info("METHOD REQUEST: " + request.getMethod());
            logger.info("REQUEST: " + request.getRequestURI());
            String authorizationToken = request.getHeader("Authorization");
            if(authorizationToken == null || authorizationToken.isEmpty()){
                throw new CustomIOException(MessageConstant.TOKEN_REQUIRED, HttpStatus.UNAUTHORIZED);
            }else if(!authorizationToken.startsWith("Bearer ")){
                throw new CustomIOException(MessageConstant.TOKEN_NOT_WITH_BEARER, HttpStatus.UNAUTHORIZED);
            }

            String jwtToken = authorizationToken.substring(7);

            if(jwtToken.isEmpty()){
                throw new CustomIOException(MessageConstant.TOKEN_REQUIRED, HttpStatus.UNAUTHORIZED);
            }

            String email = null;
            try {
                email = jwtTokenUtil.getUsernameFromToken(jwtToken);
            }catch (Exception e){
                if(e.getMessage().toLowerCase().contains("EXPIRED".toLowerCase())){
                    throw new CustomIOException(MessageConstant.TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED);
                }else{
                    throw new CustomIOException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = jwtUserDetailService.loadUserByUsername(email);
                if(jwtTokenUtil.validateToken(jwtToken, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else{
                    throw new CustomIOException(MessageConstant.TOKEN_NOT_VALID, HttpStatus.UNAUTHORIZED);
                }
            }
            filterChain.doFilter(request, response);
        } catch (CustomIOException e){
            setResponseError(response, e.httpStatus, convertObjectToJson(e.httpStatus, e.message));
        } catch (Exception e){
            if(e.getMessage() != null && e.getMessage().toLowerCase().contains("EXPIRED".toLowerCase())){
                setResponseError(response, HttpStatus.UNAUTHORIZED, convertObjectToJson(HttpStatus.UNAUTHORIZED, MessageConstant.TOKEN_EXPIRED));
            }else{
                setResponseError(response, HttpStatus.INTERNAL_SERVER_ERROR, convertObjectToJson(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
            }
        }
    }

    private void setResponseError(HttpServletResponse response, HttpStatus httpStatus, String mapper) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.getWriter().write(mapper);
    }

    private String convertObjectToJson(HttpStatus httpStatus, String message) throws JsonProcessingException {
        BaseResponse baseResponse = new BaseResponse<>(httpStatus.value(), message);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(baseResponse);
    }

    private Boolean checkIsNotFiltering(HttpServletRequest request, String prefix, String path){
        return Objects.equals(request.getRequestURI(), "/" + prefix + path);
    }
}
