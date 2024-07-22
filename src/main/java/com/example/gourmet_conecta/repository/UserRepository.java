package com.example.gourmet_conecta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.gourmet_conecta.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
  UserDetails findByEmail(String email);
}
