package com.kuba.gymtrackerapi.workoutsessionexerciseset;

import com.kuba.gymtrackerapi.exceptions.NotFoundException;
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
class WorkoutSessionExerciseSetServiceTest {

    @Mock
    private WorkoutSessionExerciseSetRepository workoutSessionExerciseSetRepository;

    @InjectMocks
    private WorkoutSessionExerciseSetService workoutSessionExerciseSetService;

    private final Long workoutSessionExerciseSetId = 1L;
    private WorkoutSessionExerciseSet workoutSessionExerciseSet;

    @BeforeEach
    void setUp() {
        workoutSessionExerciseSet = new WorkoutSessionExerciseSet();
        workoutSessionExerciseSet.setId(workoutSessionExerciseSetId);
    }

    @Test
    public void getWorkoutSessionExerciseSetEntityById_ShouldThrowNotFoundException() {
        when(workoutSessionExerciseSetRepository.findById(workoutSessionExerciseSetId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId));

        verify(workoutSessionExerciseSetRepository, times(1)).findById(workoutSessionExerciseSetId);
    }

    @Test
    public void getWorkoutSessionExerciseSetEntityById_ShouldReturnWorkoutSessionExerciseSet() {
        when(workoutSessionExerciseSetRepository.findById(workoutSessionExerciseSetId)).thenReturn(Optional.of(workoutSessionExerciseSet));

        WorkoutSessionExerciseSet foundWorkoutSessionExerciseSet = workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId);

        assertNotNull(foundWorkoutSessionExerciseSet);
        assertEquals(workoutSessionExerciseSetId, foundWorkoutSessionExerciseSet.getId());
        verify(workoutSessionExerciseSetRepository, times(1)).findById(workoutSessionExerciseSetId);
    }

    @Test
    public void saveWorkoutSessionExerciseSet_ShouldSave() {
        when(workoutSessionExerciseSetRepository.save(any(WorkoutSessionExerciseSet.class))).thenReturn(workoutSessionExerciseSet);

        WorkoutSessionExerciseSet createdWorkoutSessionExerciseSet = workoutSessionExerciseSetService.saveWorkoutSessionExerciseSet(workoutSessionExerciseSet);

        assertNotNull(createdWorkoutSessionExerciseSet);
        assertEquals(workoutSessionExerciseSetId, createdWorkoutSessionExerciseSet.getId());
        verify(workoutSessionExerciseSetRepository, times(1)).save(workoutSessionExerciseSet);
    }

    @Test
    public void deleteWorkoutSessionExerciseSetById_ShouldDelete() {
        doNothing().when(workoutSessionExerciseSetRepository).deleteById(workoutSessionExerciseSetId);

        workoutSessionExerciseSetService.deleteWorkoutSessionExerciseSetById(workoutSessionExerciseSetId);

        verify(workoutSessionExerciseSetRepository, times(1)).deleteById(workoutSessionExerciseSetId);
    }
}