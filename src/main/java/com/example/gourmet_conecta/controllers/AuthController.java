package com.example.gourmet_conecta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gourmet_conecta.domain.user.AuthenticationDTO;
import com.example.gourmet_conecta.domain.user.LoginResponseDTO;
import com.example.gourmet_conecta.domain.user.User;
import com.example.gourmet_conecta.infra.security.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO authenticationDTO) {
    UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
        authenticationDTO.email(),
        authenticationDTO.password());

    Authentication auth = this.authenticationManager.authenticate(usernamePassword);

    String token = tokenService.generateToken((User) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDTO(token));
  }
}
