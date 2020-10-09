package com.example.authenservice.domain.repositories;

import com.example.authenservice.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(String email);

    User findUserById(int id);
}
