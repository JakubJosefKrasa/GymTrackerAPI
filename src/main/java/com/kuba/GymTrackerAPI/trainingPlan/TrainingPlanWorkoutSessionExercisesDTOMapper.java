package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.workoutSession.WorkoutSession;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExerciseDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class TrainingPlanWorkoutSessionExercisesDTOMapper implements Function<WorkoutSession, TrainingPlanWorkoutSessionExercisesDTO> {
    private final WorkoutSessionExerciseDTOMapper workoutSessionExerciseDTOMapper;

    @Override
    public TrainingPlanWorkoutSessionExercisesDTO apply(WorkoutSession workoutSession) {
        return new TrainingPlanWorkoutSessionExercisesDTO(
                workoutSession.getTrainingPlan().getId(),
                workoutSession.getTrainingPlan().getTrainingPlanName(),
                workoutSession.getWorkoutSessionExercises()
                        .stream()
                        .map(workoutSessionExerciseDTOMapper)
                        .toList()
        );
    }
}
