package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.exercise.ExerciseSetDTO;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.SetMapper;
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
