package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("workout-sessions")
@RequiredArgsConstructor
public class WorkoutSessionController {
    private final WorkoutSessionService workoutSessionService;

    @GetMapping
    public ResponseEntity<List<WorkoutSessionDTO>> getWorkoutSessionsByUser() {
        List<WorkoutSessionDTO> workoutSessions = workoutSessionService.getWorkoutSessionsByUser();

        return new ResponseEntity<>(workoutSessions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionDTO> getWorkoutSessionById(@PathVariable Long id) {
        WorkoutSessionDTO workoutSession = workoutSessionService.getWorkoutSessionById(id);

        return new ResponseEntity<>(workoutSession, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WorkoutSessionDTO> createWorkoutSession(@Valid @RequestBody WorkoutSessionRequestDTO workoutSessionRequest) {
        WorkoutSessionDTO workoutSession = workoutSessionService.createWorkoutSession(workoutSessionRequest);

        return new ResponseEntity<>(workoutSession, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutSessionById(@PathVariable Long id) {
        workoutSessionService.deleteWorkoutSessionById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{workoutSessionId}/workout-session-exercises/{workoutSessionExerciseId}/workout-session-exercise-sets")
    public ResponseEntity<WorkoutSessionDTO> createExerciseSet(
            @PathVariable Long workoutSessionId,
            @PathVariable Long workoutSessionExerciseId,
            @Valid @RequestBody WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest
    ) {
        WorkoutSessionDTO workoutSession = workoutSessionService.createExerciseSet(
                workoutSessionId,
                workoutSessionExerciseId,
                workoutSessionExerciseSetRequest
        );

        return new ResponseEntity<>(workoutSession, HttpStatus.CREATED);
    }

    @DeleteMapping("/{workoutSessionId}/workout-session-exercises/{workoutSessionExerciseId}/workout-session-exercise-sets/{workoutSessionExerciseSetId}")
    public ResponseEntity<WorkoutSessionDTO> deleteExerciseSetById(
            @PathVariable Long workoutSessionId,
            @PathVariable Long workoutSessionExerciseId,
            @PathVariable Long workoutSessionExerciseSetId
    ) {
        workoutSessionService.deleteExerciseSetById(workoutSessionId, workoutSessionExerciseId, workoutSessionExerciseSetId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{workoutSessionId}/workout-session-exercises/{workoutSessionExerciseId}/workout-session-exercise-sets/{workoutSessionExerciseSetId}")
    public ResponseEntity<WorkoutSessionDTO> editExerciseSet(
            @PathVariable Long workoutSessionId,
            @PathVariable Long workoutSessionExerciseId,
            @PathVariable Long workoutSessionExerciseSetId,
            @Valid @RequestBody WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest
    ) {
        WorkoutSessionDTO workoutSession = workoutSessionService.editExerciseSet(
                workoutSessionId,
                workoutSessionExerciseId,
                workoutSessionExerciseSetId,
                workoutSessionExerciseSetRequest
        );

        return new ResponseEntity<>(workoutSession, HttpStatus.OK);
    }
}
