package com.wamk.uber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.wamk.uber.entities.user.User;

public interface UserRepository extends JpaRepository<User, String>{

	UserDetails findByLogin(String login);
}
