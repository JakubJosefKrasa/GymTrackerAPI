package com.kuba.gymtrackerapi.workoutsession;

import com.kuba.gymtrackerapi.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    List<WorkoutSession> findByUser(User user);

    Optional<WorkoutSession> findByIdAndUser(Long id, User user);
}
