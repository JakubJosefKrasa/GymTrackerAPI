package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import org.springframework.security.core.Authentication;

public interface ExerciseService {
    PaginationDTO<ExerciseDTO> getExercisesByUser(int pageNumber, int pageSize, Authentication authenticatedUser);
    ExerciseDTO createExercise(ExerciseRequest exerciseRequest, Authentication authenticatedUser);
    void deleteExerciseById(Long id, Authentication authenticatedUser);
    ExerciseDTO changeExerciseName(Long id, ExerciseRequest exerciseRequest, Authentication authenticatedUser);
}
