package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {

    Page<TrainingPlan> findByUser(Pageable pageable, User user);

    Optional<TrainingPlan> findByIdAndUser(Long id, User user);

    @EntityGraph(attributePaths = { "exercises" })
    Optional<TrainingPlan> findWithExercisesByIdAndUser(Long id, User user);

    @EntityGraph(attributePaths = { "exercises", "workoutSessions" })
    Optional<TrainingPlan> findWithExercisesWorkoutSessionsByIdAndUser(Long id, User user);
}
