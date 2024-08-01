package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.SetDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ExerciseSetDTOMapper implements Function<WorkoutSessionExercise, ExerciseSetDTO> {
    private final SetDTOMapper setDTOMapper;

    @Override
    public ExerciseSetDTO apply(WorkoutSessionExercise workoutSessionExercise) {
        return new ExerciseSetDTO(
                workoutSessionExercise.getExercise().getId(),
                workoutSessionExercise.getExercise().getExerciseName(),
                workoutSessionExercise.getWorkoutSessionExerciseSets()
                        .stream()
                        .map(setDTOMapper)
                        .toList()
        );
    }
}
