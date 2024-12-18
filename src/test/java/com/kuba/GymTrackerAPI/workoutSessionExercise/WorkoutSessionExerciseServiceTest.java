package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.workoutsession.WorkoutSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutSessionExerciseServiceTest {
    @Mock
    private WorkoutSessionExerciseRepository workoutSessionExerciseRepository;

    @InjectMocks
    private WorkoutSessionExerciseService workoutSessionExerciseService;

    private final Long workoutSessionId = 1L;
    private final Long workoutSessionExerciseId = 1L;

    private WorkoutSession workoutSession;
    private WorkoutSessionExercise workoutSessionExercise;

    @BeforeEach
    void setUp() {
        workoutSession = new WorkoutSession();
        workoutSession.setId(workoutSessionId);

        workoutSessionExercise = new WorkoutSessionExercise();
        workoutSessionExercise.setId(workoutSessionExerciseId);
    }

    @Test
    public void getWorkoutSessionExerciseEntityByIdAndWorkoutSession_ShouldThrowNotFoundException() {
        when(workoutSessionExerciseRepository.findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession));
        verify(workoutSessionExerciseRepository, times(1)).findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession);
    }

    @Test
    public void getWorkoutSessionExerciseEntityByIdAndWorkoutSession_ShouldReturnWorkoutSessionExercise() {
        when(workoutSessionExerciseRepository.findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession)).thenReturn(Optional.of(workoutSessionExercise));

        WorkoutSessionExercise foundWorkoutSessionExercise = workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession);

        assertNotNull(foundWorkoutSessionExercise);
        assertEquals(workoutSessionExerciseId, foundWorkoutSessionExercise.getId());
        verify(workoutSessionExerciseRepository, times(1)).findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession);
    }
}