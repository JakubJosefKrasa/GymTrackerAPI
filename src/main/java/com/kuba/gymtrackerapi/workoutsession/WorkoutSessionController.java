package com.kuba.gymtrackerapi.workoutsession;

import com.kuba.gymtrackerapi.workoutsession.dto.WorkoutSessionDTO;
import com.kuba.gymtrackerapi.workoutsession.dto.WorkoutSessionRequestDTO;
import com.kuba.gymtrackerapi.workoutsessionexerciseset.WorkoutSessionExerciseSetRequestDTO;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<WorkoutSessionDTO> createWorkoutSession(
            @Valid @RequestBody WorkoutSessionRequestDTO workoutSessionRequest
    ) {
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
            @Valid @RequestBody WorkoutSessionExerciseSetRequestDTO workoutSessionExerciseSetRequest
    ) {
        WorkoutSessionDTO workoutSession = workoutSessionService.createExerciseSet(workoutSessionId,
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
        workoutSessionService.deleteExerciseSetById(workoutSessionId,
                                                    workoutSessionExerciseId,
                                                    workoutSessionExerciseSetId
        );

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{workoutSessionId}/workout-session-exercises/{workoutSessionExerciseId}/workout-session-exercise-sets/{workoutSessionExerciseSetId}")
    public ResponseEntity<WorkoutSessionDTO> editExerciseSet(
            @PathVariable Long workoutSessionId,
            @PathVariable Long workoutSessionExerciseId,
            @PathVariable Long workoutSessionExerciseSetId,
            @Valid @RequestBody WorkoutSessionExerciseSetRequestDTO workoutSessionExerciseSetRequest
    ) {
        WorkoutSessionDTO workoutSession = workoutSessionService.editExerciseSet(workoutSessionId,
                                                                                 workoutSessionExerciseId,
                                                                                 workoutSessionExerciseSetId,
                                                                                 workoutSessionExerciseSetRequest
        );

        return new ResponseEntity<>(workoutSession, HttpStatus.OK);
    }
}
