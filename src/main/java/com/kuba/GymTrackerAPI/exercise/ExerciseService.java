package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;

import java.util.List;

public interface ExerciseService {
    PaginationDTO<ExerciseDTO> getExercisesByUser(int pageNumber, int pageSize);
    List<ExerciseDTO> getExercisesNotInTrainingPlan(Long trainingPlanId);
    ExerciseDTO createExercise(ExerciseRequest exerciseRequest);
    void deleteExerciseById(Long id);
    ExerciseDTO changeExerciseName(Long id, ExerciseRequest exerciseRequest);
}
