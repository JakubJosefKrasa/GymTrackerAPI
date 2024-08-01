package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    List<WorkoutSession> findByUser(User user);
    Optional<WorkoutSession> findByIdAndUser(Long id, User user);
}
