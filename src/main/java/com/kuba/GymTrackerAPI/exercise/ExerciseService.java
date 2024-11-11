package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.user.User;

import java.util.List;

public interface ExerciseService {
    Exercise getExerciseEntityById(Long id, User user);
    PaginationDTO<ExerciseDTO> getExercisesByUser(int pageNumber, int pageSize);
    List<ExerciseDTO> getExercisesNotInTrainingPlan(Long trainingPlanId);
    ExerciseDTO createExercise(ExerciseRequest exerciseRequest);
    void deleteExerciseById(Long id);
    ExerciseDTO changeExerciseName(Long id, ExerciseRequest exerciseRequest);
}
