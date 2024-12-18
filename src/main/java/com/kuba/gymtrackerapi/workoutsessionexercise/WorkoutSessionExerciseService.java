package com.kuba.gymtrackerapi.workoutsessionexercise;

import com.kuba.gymtrackerapi.exceptions.NotFoundException;
import com.kuba.gymtrackerapi.workoutsession.WorkoutSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutSessionExerciseService {

    private final WorkoutSessionExerciseRepository workoutSessionExerciseRepository;

    public WorkoutSessionExercise getWorkoutSessionExerciseEntityByIdAndWorkoutSession(
            Long workoutSessionExerciseId, WorkoutSession workoutSession
    ) {
        log.info(
                "[METHOD]: getWorkoutSessionExerciseEntityByIdAndWorkoutSession - Fetching workoutSessionExercise by ID: {} and workoutSession_id: {}",
                workoutSessionExerciseId,
                workoutSession.getId()
        );

        return workoutSessionExerciseRepository.findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession)
                                               .orElseThrow(() -> {
                                                   log.warn(
                                                           "[METHOD]: getWorkoutSessionExerciseEntityByIdAndWorkoutSession - workoutSessionExercise was not found by ID: {} and workoutSession_id: {}",
                                                           workoutSessionExerciseId,
                                                           workoutSession.getId()
                                                   );

                                                   return new NotFoundException("Cvik v tréninku nenalezen!");
                                               });
    }
}
