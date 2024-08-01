package com.kuba.GymTrackerAPI.workoutSessionExerciseSet;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class SetDTOMapper implements Function<WorkoutSessionExerciseSet, SetDTO> {
    @Override
    public SetDTO apply(WorkoutSessionExerciseSet workoutSessionExerciseSet) {
        return new SetDTO(
                workoutSessionExerciseSet.getId(),
                workoutSessionExerciseSet.getRepetitions(),
                workoutSessionExerciseSet.getWeight()
        );
    }
}
