package com.kuba.gymtrackerapi.exercise;

import com.kuba.gymtrackerapi.exercise.dto.ExerciseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "exerciseName", source = "exerciseName")
    ExerciseDTO toExerciseDTO(Exercise exercise);
}
