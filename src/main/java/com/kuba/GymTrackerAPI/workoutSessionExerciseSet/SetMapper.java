package com.kuba.GymTrackerAPI.workoutSessionExerciseSet;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SetMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "repetitions", source = "repetitions")
    @Mapping(target = "weight", source = "weight")
    SetDTO toSetDTO(WorkoutSessionExerciseSet workoutSessionExerciseSet);
}
