package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    List<WorkoutSession> findByUser(User user);

    Optional<WorkoutSession> findByIdAndUser(Long id, User user);
}
