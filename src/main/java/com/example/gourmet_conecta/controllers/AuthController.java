package com.example.gourmet_conecta.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gourmet_conecta.domain.user.AuthenticationDTO;
import com.example.gourmet_conecta.domain.user.LoginResponseDTO;
import com.example.gourmet_conecta.domain.user.RegisterDTO;
import com.example.gourmet_conecta.domain.user.UserEntity;

import com.example.gourmet_conecta.infra.security.TokenService;
import com.example.gourmet_conecta.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

  // @Autowired
  // private ApplicationContext context;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository repository;

  // AuthController(AuthenticationManager authenticationManager) {
  // this.authenticationManager = authenticationManager;
  // }

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login(@RequestBody AuthenticationDTO authenticationDTO) {
    System.out.println("authenticationDTO: \n" + authenticationDTO);

    Authentication usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(),
        authenticationDTO.password());

    System.out.println("usernamePassword: \n" + usernamePassword);
    Authentication auth = this.authenticationManager.authenticate(usernamePassword);

    System.out.println("auth" + auth);

    String token = tokenService.generateToken((UserEntity) auth.getPrincipal());
    // String token = "sddfd";

    System.out.println("token" + token);

    return ResponseEntity.ok(new LoginResponseDTO(token));
  }

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
    System.out.println("authenticationDTO: \n" + registerDTO);

    if (this.repository.findByEmail(registerDTO.email()) != null) {
      return ResponseEntity.badRequest().body("Email already registered");
    }

    String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());
    UserEntity user = new UserEntity(registerDTO.firstName(), registerDTO.lastName(), registerDTO.email(),
        encryptedPassword);

    this.repository.save(user);

    return ResponseEntity.ok("register");
  }
}
