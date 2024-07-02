package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Page<Exercise> findByUser(Pageable pageable, User user);
    Optional<Exercise> findByIdAndUser(Long id, User user);
}
