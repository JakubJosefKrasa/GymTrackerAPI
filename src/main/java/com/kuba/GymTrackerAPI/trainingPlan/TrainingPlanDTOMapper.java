package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exercise.ExerciseDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanDTOMapper implements Function<TrainingPlan, TrainingPlanDTO> {
    private final ExerciseDTOMapper exerciseDTOMapper;

    @Override
    public TrainingPlanDTO apply(TrainingPlan trainingPlan) {
        return new TrainingPlanDTO(
                trainingPlan.getId(),
                trainingPlan.getTrainingPlanName(),
                trainingPlan.getUser().getId(),
                trainingPlan.getExercises()
                        .stream()
                        .map(exerciseDTOMapper)
                        .collect(Collectors.toSet())
        );
    }
}
