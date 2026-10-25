package com.example.greenbyte.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.greenbyte.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
