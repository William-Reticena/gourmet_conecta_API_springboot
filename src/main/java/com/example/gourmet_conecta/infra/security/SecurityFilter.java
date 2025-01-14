package com.example.gourmet_conecta.infra.security;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.gourmet_conecta.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
  @Autowired
  TokenService tokenService;

  @Autowired
  UserRepository userRepository;

  private static final List<String> PUBLIC_URLS = List.of("/auth/login", "/auth/register");

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    if (!PUBLIC_URLS.contains(requestURI)) {

      String token = this.recoverToken(request);

      if (token != null) {
        String login = tokenService.validateToken(token);
        UserDetails userDetails = userRepository.findByEmail(login);

        if (userDetails != null) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
              null,
              userDetails.getAuthorities());

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }

    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");

    if (authHeader == null)
      return null;

    return authHeader.replace("Bearer ", "");
  }
}
