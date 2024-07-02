package com.kuba.GymTrackerAPI.exercise;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ExerciseDTOMapper implements Function<Exercise, ExerciseDTO> {
    @Override
    public ExerciseDTO apply(Exercise exercise) {
        return new ExerciseDTO(
                exercise.getId(),
                exercise.getExerciseName(),
                exercise.getUser().getId()
        );
    }
}
