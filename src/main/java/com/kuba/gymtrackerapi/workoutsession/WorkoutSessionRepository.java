package com.kuba.gymtrackerapi.workoutsession;

import com.kuba.gymtrackerapi.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    @EntityGraph(attributePaths = "trainingPlan")
    List<WorkoutSession> findByUser(User user);

    Optional<WorkoutSession> findByIdAndUser(Long id, User user);

    @EntityGraph(
            attributePaths = {
                    "trainingPlan",
                    "workoutSessionExercises",
                    "workoutSessionExercises.exercise",
                    "workoutSessionExercises.workoutSessionExerciseSets"
            }
    )
    Optional<WorkoutSession> findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(Long id, User user);
}
