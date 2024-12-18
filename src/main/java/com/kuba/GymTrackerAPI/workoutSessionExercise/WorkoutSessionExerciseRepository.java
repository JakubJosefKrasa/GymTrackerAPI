package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.workoutSession.WorkoutSession;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSessionExerciseRepository extends JpaRepository<WorkoutSessionExercise, Long> {

    Optional<WorkoutSessionExercise> findByIdAndWorkoutSession(Long id, WorkoutSession workoutSession);
}
