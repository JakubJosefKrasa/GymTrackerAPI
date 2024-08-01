package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exercise.ExerciseDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanExercisesDTOMapper implements Function<TrainingPlan, TrainingPlanExercisesDTO> {
    private final ExerciseDTOMapper exerciseDTOMapper;

    @Override
    public TrainingPlanExercisesDTO apply(TrainingPlan trainingPlan) {
        return new TrainingPlanExercisesDTO(
                trainingPlan.getId(),
                trainingPlan.getTrainingPlanName(),
                trainingPlan.getExercises()
                        .stream()
                        .map(exerciseDTOMapper)
                        .collect(Collectors.toSet())
        );
    }
}
