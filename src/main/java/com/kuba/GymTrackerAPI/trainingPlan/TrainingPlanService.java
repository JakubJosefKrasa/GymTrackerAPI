package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import org.springframework.security.core.Authentication;

public interface TrainingPlanService {
    PaginationDTO<TrainingPlanDTO> getTrainingPlansByUser(int pageNumber, int pageSize, Authentication authenticatedUser);
    TrainingPlanDTO getTrainingPlanById(Long id, Authentication authenticatedUser);
    TrainingPlanDTO createTrainingPlan(TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser);
    void deleteTrainingPlanById(Long id, Authentication authenticatedUser);
    TrainingPlanDTO changeTrainingPlanName(Long id, TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser);
    TrainingPlanDTO addExerciseInTrainingPlan(Long trainingPlanId, Long exerciseId, Authentication authenticatedUser);
    void removeExerciseFromTrainingPlan(Long trainingPlanId, Long exerciseId, Authentication authenticatedUser);
}
