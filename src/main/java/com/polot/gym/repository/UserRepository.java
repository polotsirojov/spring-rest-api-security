package com.polot.gym.repository;

import com.polot.gym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameIgnoreCase(String username);
    int countAllByUsernameContainingIgnoreCase(String username);

}
