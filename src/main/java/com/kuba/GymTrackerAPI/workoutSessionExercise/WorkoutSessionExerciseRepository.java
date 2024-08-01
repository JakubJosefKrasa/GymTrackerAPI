package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.workoutSession.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutSessionExerciseRepository extends JpaRepository<WorkoutSessionExercise, Long> {
    Optional<WorkoutSessionExercise> findByIdAndWorkoutSession(Long id, WorkoutSession workoutSession);
}
