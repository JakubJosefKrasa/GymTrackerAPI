package com.kuba.gymtrackerapi.workoutsessionexerciseset;

import com.kuba.gymtrackerapi.workoutsessionexerciseset.dto.WorkoutSessionExerciseSetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkoutSessionExerciseSetMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "repetitions", source = "repetitions")
    @Mapping(target = "weight", source = "weight")
    WorkoutSessionExerciseSetDTO toSetDTO(WorkoutSessionExerciseSet workoutSessionExerciseSet);
}
