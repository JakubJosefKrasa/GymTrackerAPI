package com.kuba.GymTrackerAPI.workoutSessionExerciseSet;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutSessionExerciseSetService {

    private final WorkoutSessionExerciseSetRepository workoutSessionExerciseSetRepository;

    public WorkoutSessionExerciseSet getWorkoutSessionExerciseSetEntityById(Long workoutSessionExerciseSetId) {
        log.info(
                "[METHOD]: getWorkoutSessionExerciseSetEntityById - Fetching workoutSessionExerciseSet by ID: {}",
                workoutSessionExerciseSetId
        );

        return workoutSessionExerciseSetRepository.findById(workoutSessionExerciseSetId).orElseThrow(() -> {
            log.warn(
                    "[METHOD]: getWorkoutSessionExerciseSetEntityById - workoutSessionExerciseSet was not found by ID: {}",
                    workoutSessionExerciseSetId
            );

            return new NotFoundException("Série nenalezena");
        });
    }

    public WorkoutSessionExerciseSet saveWorkoutSessionExerciseSet(
            WorkoutSessionExerciseSet workoutSessionExerciseSet
    ) {
        log.info("[METHOD]: saveWorkoutSessionExerciseSet - Saving workoutSessionExerciseSet");

        return workoutSessionExerciseSetRepository.save(workoutSessionExerciseSet);
    }

    public void deleteWorkoutSessionExerciseSetById(Long workoutSessionExerciseSetId) {
        log.info(
                "[METHOD]: deleteWorkoutSessionExerciseSetById - Deleting workoutSessionExerciseSet by ID: {}",
                workoutSessionExerciseSetId
        );

        workoutSessionExerciseSetRepository.deleteById(workoutSessionExerciseSetId);

        log.info(
                "[METHOD]: deleteWorkoutSessionExerciseSetById - workoutSessionExerciseSet by ID: {} was deleted",
                workoutSessionExerciseSetId
        );
    }
}
