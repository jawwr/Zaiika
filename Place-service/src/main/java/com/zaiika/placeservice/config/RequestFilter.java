package com.zaiika.placeservice.config;

import com.zaiika.placeservice.model.utils.TokenDto;
import com.zaiika.placeservice.service.users.TokenRepo;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {
    @Resource(name = "tokenRepo")
    private final TokenRepo tokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt = authHeader.substring(7);
        tokenRepo.setTokenDto(new TokenDto(jwt));

        filterChain.doFilter(request, response);
    }
}
