package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.exercise.ExerciseMapper;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanDTO;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanExercisesDTO;
import com.kuba.gymtrackerapi.workoutsession.WorkoutSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ExerciseMapper.class)
public interface TrainingPlanMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "trainingPlanName", source = "trainingPlanName")
    TrainingPlanDTO toTrainingPlanDTO(TrainingPlan trainingPlan);

    @Mapping(target = "id", source = "trainingPlan.id")
    @Mapping(target = "trainingPlanName", source = "trainingPlan.trainingPlanName")
    TrainingPlanDTO toTrainingPlanDTO(WorkoutSession workoutSession);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "trainingPlanName", source = "trainingPlanName")
    @Mapping(target = "exercises", source = "exercises")
    TrainingPlanExercisesDTO toTrainingPlanExercisesDTO(TrainingPlan trainingPlan);
}
