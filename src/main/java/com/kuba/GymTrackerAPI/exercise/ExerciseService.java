package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseService {
    PaginationDTO<ExerciseDTO> getExercisesByUser(int pageNumber, int pageSize, Authentication authenticatedUser);
    List<ExerciseDTO> getExercisesNotInTrainingPlan(Long trainingPlanId, Authentication authenticatedUser);
    ExerciseDTO createExercise(ExerciseRequest exerciseRequest, Authentication authenticatedUser);
    void deleteExerciseById(Long id, Authentication authenticatedUser);
    ExerciseDTO changeExerciseName(Long id, ExerciseRequest exerciseRequest, Authentication authenticatedUser);
}
