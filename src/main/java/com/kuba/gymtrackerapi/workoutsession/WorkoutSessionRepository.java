package com.kuba.gymtrackerapi.workoutsession;

import com.kuba.gymtrackerapi.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

    @EntityGraph(attributePaths = {"trainingPlan", "workoutSessionExercises"})
    List<WorkoutSession> findByUser(User user);

    /*
    @EntityGraph(
            attributePaths = {
                    "trainingPlan",
                    "workoutSessionExercises",
                    "workoutSessionExercises.exercise",
                    "workoutSessionExercises.workoutSessionExerciseSets"
            }
    )
    Optional<WorkoutSession> findByIdAndUser(Long id, User user);
*/

    @Query(
            """
               SELECT ws FROM WorkoutSession ws
               LEFT JOIN FETCH ws.trainingPlan
               LEFT JOIN FETCH ws.workoutSessionExercises wse
               LEFT JOIN FETCH wse.exercise e
               LEFT JOIN FETCH wse.workoutSessionExerciseSets
               WHERE ws.id = :id AND ws.user = :user
            """
    )
    Optional<WorkoutSession> findByIdAndUser(@Param("id") Long id, @Param("user") User user);
}
