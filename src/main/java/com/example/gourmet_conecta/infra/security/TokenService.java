package com.example.gourmet_conecta.infra.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.gourmet_conecta.domain.user.UserEntity;

@Service
public class TokenService {
  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(UserEntity user) {
    System.out.println("secret: " + secret);
    System.out.println("user: " + user);
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      String token = JWT.create()
          .withIssuer("Gourmet Conecta")
          .withSubject(user.getEmail())
          .sign(algorithm);

      System.out.println("token: 111" + token);

      return token;
    } catch (JWTCreationException exception) {
      throw new RuntimeException("Error generating token", exception);
    }
  }

  public String validateToken(String token) {
    try {
      System.out.println("token: " + token);

      Algorithm algorithm = Algorithm.HMAC256(secret);
      String subject = JWT.require(algorithm)
          .withIssuer("Gourmet Conecta")
          .build()
          .verify(token)
          .getSubject();

      return subject;
    } catch (JWTVerificationException exception) {
      throw new RuntimeException("Error validating token", exception);
    }
  }
}
