package com.kuba.gymtrackerapi.workoutsession;

import com.kuba.gymtrackerapi.trainingplan.TrainingPlanWorkoutSessionExercisesDTO;
import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExerciseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = WorkoutSessionExerciseMapper.class)
public interface WorkoutSessionMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "trainingPlan", source = "workoutSession")
    WorkoutSessionDTO toWorkoutSessionDTO(WorkoutSession workoutSession);

    @Mapping(target = "id", source = "trainingPlan.id")
    @Mapping(target = "trainingPlanName", source = "trainingPlan.trainingPlanName")
    @Mapping(target = "workoutSessionExercises", source = "workoutSessionExercises")
    TrainingPlanWorkoutSessionExercisesDTO toTrainingPlanWorkoutSessionExercisesDTO(WorkoutSession workoutSession);
}
