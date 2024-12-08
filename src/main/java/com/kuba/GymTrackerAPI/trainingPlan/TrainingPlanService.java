package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.user.User;

public interface TrainingPlanService {
    TrainingPlan getTrainingPlanEntityById(Long id, User user);
    PaginationDTO<TrainingPlanExercisesDTO> getTrainingPlansByUser(int pageNumber, int pageSize);
    TrainingPlanExercisesDTO getTrainingPlanById(Long id);
    TrainingPlanExercisesDTO createTrainingPlan(TrainingPlanRequest trainingPlanRequest);
    void deleteTrainingPlanById(Long id);
    TrainingPlanExercisesDTO changeTrainingPlanName(Long id, TrainingPlanRequest trainingPlanRequest);
    TrainingPlanExercisesDTO addExerciseInTrainingPlan(Long trainingPlanId, Long exerciseId);
    void removeExerciseFromTrainingPlan(Long trainingPlanId, Long exerciseId);
}
