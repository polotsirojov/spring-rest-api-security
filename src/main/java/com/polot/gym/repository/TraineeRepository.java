package com.polot.gym.repository;

import com.polot.gym.entity.Trainee;
import com.polot.gym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee,Integer> {
    Optional<Trainee> findByUser(User user);
}
