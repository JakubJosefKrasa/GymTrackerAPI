package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exercise.ExerciseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ExerciseMapper.class)
public interface TrainingPlanMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "trainingPlanName", source = "trainingPlanName")
    @Mapping(target = "exercises", source = "exercises")
    TrainingPlanExercisesDTO toTrainingPlanExercisesDTO(TrainingPlan trainingPlan);
}
