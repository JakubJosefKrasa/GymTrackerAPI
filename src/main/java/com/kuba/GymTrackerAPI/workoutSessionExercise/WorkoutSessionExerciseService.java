package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.workoutSession.WorkoutSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutSessionExerciseService {
    private final WorkoutSessionExerciseRepository workoutSessionExerciseRepository;

    public WorkoutSessionExercise getWorkoutSessionExerciseEntityByIdAndWorkoutSession(Long workoutSessionExerciseId, WorkoutSession workoutSession) {
        return workoutSessionExerciseRepository.findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession).orElseThrow(() -> new NotFoundException("Cvik v tréninku nenalezen!"));
    }
}
