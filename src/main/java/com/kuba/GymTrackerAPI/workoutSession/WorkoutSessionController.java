package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("workout-sessions")
@RequiredArgsConstructor
public class WorkoutSessionController {
    private final WorkoutSessionService workoutSessionService;

    @GetMapping
    public ResponseEntity<List<WorkoutSessionDTO>> getWorkoutSessionsByUser(Authentication authenticatedUser) {
        List<WorkoutSessionDTO> workoutSessions = workoutSessionService.getWorkoutSessionsByUser(authenticatedUser);

        return new ResponseEntity<>(workoutSessions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionDTO> getWorkoutSessionById(@PathVariable Long id, Authentication authenticatedUser) {
        WorkoutSessionDTO workoutSession = workoutSessionService.getWorkoutSessionById(id, authenticatedUser);

        return new ResponseEntity<>(workoutSession, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WorkoutSessionDTO> createWorkoutSession(@RequestBody WorkoutSessionRequest workoutSessionRequest, Authentication authenticatedUser) {
        WorkoutSessionDTO workoutSession = workoutSessionService.createWorkoutSession(workoutSessionRequest, authenticatedUser);

        System.out.println(workoutSession);
        return new ResponseEntity<>(workoutSession, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutSessionById(@PathVariable Long id, Authentication authenticatedUser) {
        workoutSessionService.deleteWorkoutSessionById(id, authenticatedUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{workoutSessionId}/workout-session-exercises/{workoutSessionExerciseId}/workout-session-exercise-sets")
    public ResponseEntity<WorkoutSessionDTO> createExerciseSet(
            @PathVariable Long workoutSessionId,
            @PathVariable Long workoutSessionExerciseId,
            @Valid @RequestBody WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest,
            Authentication authenticatedUser
    ) {
        WorkoutSessionDTO workoutSession = workoutSessionService.createExerciseSet(
                workoutSessionId,
                workoutSessionExerciseId,
                workoutSessionExerciseSetRequest,
                authenticatedUser
        );

        return new ResponseEntity<>(workoutSession, HttpStatus.CREATED);
    }

    @DeleteMapping("/{workoutSessionId}/workout-session-exercises/{workoutSessionExerciseId}/workout-session-exercise-sets/{workoutSessionExerciseSetId}")
    public ResponseEntity<WorkoutSessionDTO> deleteExerciseSetById(
            @PathVariable Long workoutSessionId,
            @PathVariable Long workoutSessionExerciseId,
            @PathVariable Long workoutSessionExerciseSetId,
            Authentication authenticatedUser
    ) {
        workoutSessionService.deleteExerciseSetById(workoutSessionId, workoutSessionExerciseId, workoutSessionExerciseSetId, authenticatedUser);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{workoutSessionId}/workout-session-exercises/{workoutSessionExerciseId}/workout-session-exercise-sets/{workoutSessionExerciseSetId}")
    public ResponseEntity<WorkoutSessionDTO> editExerciseSet(
            @PathVariable Long workoutSessionId,
            @PathVariable Long workoutSessionExerciseId,
            @PathVariable Long workoutSessionExerciseSetId,
            @Valid @RequestBody WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest,
            Authentication authenticatedUser
    ) {
        WorkoutSessionDTO workoutSession = workoutSessionService.editExerciseSet(
                workoutSessionId,
                workoutSessionExerciseId,
                workoutSessionExerciseSetId,
                workoutSessionExerciseSetRequest,
                authenticatedUser
        );

        return new ResponseEntity<>(workoutSession, HttpStatus.OK);
    }
}
