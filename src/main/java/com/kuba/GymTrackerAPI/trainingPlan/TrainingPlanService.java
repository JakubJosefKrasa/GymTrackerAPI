package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import org.springframework.security.core.Authentication;

public interface TrainingPlanService {
    PaginationDTO<TrainingPlanExercisesDTO> getTrainingPlansByUser(int pageNumber, int pageSize, Authentication authenticatedUser);
    TrainingPlanExercisesDTO getTrainingPlanById(Long id, Authentication authenticatedUser);
    TrainingPlanExercisesDTO createTrainingPlan(TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser);
    void deleteTrainingPlanById(Long id, Authentication authenticatedUser);
    TrainingPlanExercisesDTO changeTrainingPlanName(Long id, TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser);
    TrainingPlanExercisesDTO addExerciseInTrainingPlan(Long trainingPlanId, Long exerciseId, Authentication authenticatedUser);
    void removeExerciseFromTrainingPlan(Long trainingPlanId, Long exerciseId, Authentication authenticatedUser);
}
