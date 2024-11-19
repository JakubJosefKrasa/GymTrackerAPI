package com.kuba.GymTrackerAPI.exercise;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "exerciseName", source = "exerciseName")
    ExerciseDTO toExerciseDTO(Exercise exercise);
}
