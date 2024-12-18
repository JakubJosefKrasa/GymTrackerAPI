package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.trainingplan.TrainingPlan;
import com.kuba.GymTrackerAPI.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Page<Exercise> findByUser(Pageable pageable, User user);

    Optional<Exercise> findByIdAndUser(Long id, User user);

    List<Exercise> findByUserAndTrainingPlansNotContaining(User user, TrainingPlan trainingPlan);
}
