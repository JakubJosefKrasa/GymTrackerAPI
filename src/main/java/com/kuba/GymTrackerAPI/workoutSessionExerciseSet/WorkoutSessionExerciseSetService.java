package com.kuba.GymTrackerAPI.workoutSessionExerciseSet;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkoutSessionExerciseSetService {
    private final WorkoutSessionExerciseSetRepository workoutSessionExerciseSetRepository;

    public WorkoutSessionExerciseSet getWorkoutSessionExerciseSetEntityById(Long workoutSessionExerciseSetId) {
        return workoutSessionExerciseSetRepository.findById(workoutSessionExerciseSetId).orElseThrow(() -> new NotFoundException("Série nenalezena"));
    }

    public WorkoutSessionExerciseSet saveWorkoutSessionExerciseSet(WorkoutSessionExerciseSet workoutSessionExerciseSet) {
        return workoutSessionExerciseSetRepository.save(workoutSessionExerciseSet);
    }

    public void deleteWorkoutSessionExerciseSetById(Long workoutSessionExerciseSetId) {
        workoutSessionExerciseSetRepository.deleteById(workoutSessionExerciseSetId);
    }
}
