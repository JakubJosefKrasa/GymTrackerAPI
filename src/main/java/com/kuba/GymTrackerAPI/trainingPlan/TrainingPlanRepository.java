package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    Page<TrainingPlan> findByUser(Pageable pageable, User user);
    Optional<TrainingPlan> findByIdAndUser(Long id, User user);
}
