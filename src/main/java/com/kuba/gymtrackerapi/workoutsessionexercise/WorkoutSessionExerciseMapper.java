package com.kuba.gymtrackerapi.workoutsessionexercise;

import com.kuba.gymtrackerapi.exercise.dto.ExerciseSetDTO;
import com.kuba.gymtrackerapi.workoutsessionexercise.dto.WorkoutSessionExerciseDTO;
import com.kuba.gymtrackerapi.workoutsessionexerciseset.SetMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SetMapper.class)
public interface WorkoutSessionExerciseMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "exercise", source = "workoutSessionExercise")
    WorkoutSessionExerciseDTO toWorkoutSessionExerciseDTO(WorkoutSessionExercise workoutSessionExercise);

    @Mapping(target = "id", source = "exercise.id")
    @Mapping(target = "exerciseName", source = "exercise.exerciseName")
    @Mapping(target = "sets", source = "workoutSessionExerciseSets")
    ExerciseSetDTO toExerciseSetDTO(WorkoutSessionExercise workoutSessionExercise);
}
