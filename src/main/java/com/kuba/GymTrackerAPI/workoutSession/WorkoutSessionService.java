package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetRequest;

import java.util.List;

public interface WorkoutSessionService {
    WorkoutSession getWorkoutSessionEntityById(Long workoutSessionId, User user);
    List<WorkoutSessionDTO> getWorkoutSessionsByUser();
    WorkoutSessionDTO getWorkoutSessionById(Long id);
    WorkoutSessionDTO createWorkoutSession(WorkoutSessionRequest workoutSessionRequest);
    void deleteWorkoutSessionById(Long id);
    WorkoutSessionDTO createExerciseSet(Long workoutSessionId, Long workoutSessionExerciseId, WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest);
    void deleteExerciseSetById(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId);
    WorkoutSessionDTO editExerciseSet(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId, WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest);
}
