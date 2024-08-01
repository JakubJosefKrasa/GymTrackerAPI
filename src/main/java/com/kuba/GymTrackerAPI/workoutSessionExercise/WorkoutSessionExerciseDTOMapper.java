package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.exercise.ExerciseSetDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class WorkoutSessionExerciseDTOMapper implements Function<WorkoutSessionExercise, WorkoutSessionExerciseDTO> {
    private final ExerciseSetDTOMapper exerciseSetDTOMapper;

    @Override
    public WorkoutSessionExerciseDTO apply(WorkoutSessionExercise workoutSessionExercise) {
        return new WorkoutSessionExerciseDTO(
                workoutSessionExercise.getId(),
                exerciseSetDTOMapper.apply(workoutSessionExercise)
        );
    }
}
