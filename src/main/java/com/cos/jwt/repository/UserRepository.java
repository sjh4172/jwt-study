package com.cos.jwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cos.jwt.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByUsername(String Username);
}
