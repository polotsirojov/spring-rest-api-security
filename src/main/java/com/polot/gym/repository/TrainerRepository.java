package com.polot.gym.repository;

import com.polot.gym.entity.Trainer;
import com.polot.gym.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Integer> {
    Optional<Trainer> findByUser(User user);

    @Query("select t from Trainer t where t.user.isActive and SIZE(t.trainings) = 0")
    List<Trainer> getNotAssignedTrainers();

    List<Trainer> findAllByUser_usernameIn(List<String> collect);
}
