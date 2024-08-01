package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlanWorkoutSessionExercisesDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class WorkoutSessionDTOMapper implements Function<WorkoutSession, WorkoutSessionDTO> {
    private final TrainingPlanWorkoutSessionExercisesDTOMapper trainingPlanWorkoutSessionExercisesDTOMapper;

    @Override
    public WorkoutSessionDTO apply(WorkoutSession workoutSession) {
        return new WorkoutSessionDTO(
                workoutSession.getId(),
                workoutSession.getDate(),
                trainingPlanWorkoutSessionExercisesDTOMapper.apply(workoutSession)
        );
    }
}
