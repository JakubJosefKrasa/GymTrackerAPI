package com.kuba.gymtrackerapi.workoutsession;

import com.kuba.gymtrackerapi.trainingplan.TrainingPlanMapper;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanWorkoutSessionExercisesDTO;
import com.kuba.gymtrackerapi.workoutsession.dto.WorkoutSessionDTO;
import com.kuba.gymtrackerapi.workoutsession.dto.WorkoutSessionExercisesDTO;
import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExerciseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { TrainingPlanMapper.class, WorkoutSessionExerciseMapper.class })
public interface WorkoutSessionMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "trainingPlan", source = "workoutSession")
    WorkoutSessionDTO toWorkoutSessionDTO(WorkoutSession workoutSession);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "trainingPlan", source = "workoutSession")
    WorkoutSessionExercisesDTO toWorkoutSessionExercisesDTO(WorkoutSession workoutSession);

    @Mapping(target = "id", source = "trainingPlan.id")
    @Mapping(target = "trainingPlanName", source = "trainingPlan.trainingPlanName")
    @Mapping(target = "workoutSessionExercises", source = "workoutSessionExercises")
    TrainingPlanWorkoutSessionExercisesDTO toTrainingPlanWorkoutSessionExercisesDTO(WorkoutSession workoutSession);
}
