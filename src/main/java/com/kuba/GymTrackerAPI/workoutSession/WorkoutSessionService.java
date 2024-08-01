package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetRequest;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface WorkoutSessionService {
    List<WorkoutSessionDTO> getWorkoutSessionsByUser(Authentication authenticatedUser);
    WorkoutSessionDTO getWorkoutSessionById(Long id, Authentication authenticatedUser);
    WorkoutSessionDTO createWorkoutSession(WorkoutSessionRequest workoutSessionRequest, Authentication authenticatedUser);
    void deleteWorkoutSessionById(Long id, Authentication authenticatedUser);
    WorkoutSessionDTO createExerciseSet(Long workoutSessionId, Long workoutSessionExerciseId, WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest, Authentication authenticatedUser);
    void deleteExerciseSetById(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId, Authentication authenticatedUser);
    WorkoutSessionDTO editExerciseSet(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId, WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest, Authentication authenticatedUser);
}
