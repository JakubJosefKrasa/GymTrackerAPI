package com.kuba.GymTrackerAPI.workoutsessionexercise;

import com.kuba.GymTrackerAPI.exercise.ExerciseSetDTO;
import com.kuba.GymTrackerAPI.workoutsessionexerciseset.SetMapper;
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
