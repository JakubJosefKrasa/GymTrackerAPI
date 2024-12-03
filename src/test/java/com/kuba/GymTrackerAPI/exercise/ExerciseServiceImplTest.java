package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.security.UserContext;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlan;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlanService;
import com.kuba.GymTrackerAPI.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceImplTest {

    @InjectMocks
    private ExerciseServiceImpl exerciseService;
    @Mock
    private UserContext userContext;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private TrainingPlanService trainingPlanService;
    @Mock
    private ExerciseMapper exerciseMapper;

    private final Long squatExerciseId = 1L;
    private final Long deadLiftExerciseId = 2L;
    private User user;
    private Exercise squatExercise;
    private Exercise deadLiftExercise;
    private ExerciseDTO squatExerciseDTO;
    private ExerciseDTO deadLiftExerciseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        squatExercise = Exercise.builder().id(squatExerciseId).exerciseName("Squat").user(user).build();
        squatExerciseDTO = new ExerciseDTO(squatExerciseId, "Squat");

        deadLiftExercise = Exercise.builder().id(deadLiftExerciseId).exerciseName("Deadlift").user(user).build();
        deadLiftExerciseDTO = new ExerciseDTO(deadLiftExerciseId, "Deadlift");
    }

    @Test
    public void getExerciseEntityById_ShouldReturnExercise() {
        when(exerciseRepository.findByIdAndUser(squatExerciseId, user)).thenReturn(Optional.of(squatExercise));

        Exercise foundExercise = exerciseService.getExerciseEntityById(squatExerciseId, user);

        assertNotNull(foundExercise);
        assertEquals(squatExercise.getId(), foundExercise.getId());
        assertEquals(squatExercise.getExerciseName(), foundExercise.getExerciseName());
        verify(exerciseRepository, times(1)).findByIdAndUser(squatExerciseId, user);
    }

    @Test
    public void getExerciseEntityById_ShouldThrowNotFoundException() {
        when(exerciseRepository.findByIdAndUser(squatExerciseId, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> exerciseService.getExerciseEntityById(squatExerciseId, user));
        verify(exerciseRepository, times(1)).findByIdAndUser(squatExerciseId, user);
    }

    @Test
    public void getExercisesByUser_ShouldReturnPagedExercise() {
        Pageable page = PageRequest.of(1, 1);
        List<Exercise> exercises = List.of(deadLiftExercise);
        Page<Exercise> exercisesPage = new PageImpl<>(exercises, page, exercises.size());

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(exerciseRepository.findByUser(page, user)).thenReturn(exercisesPage);
        when(exerciseMapper.toExerciseDTO(deadLiftExercise)).thenReturn(deadLiftExerciseDTO);

        PaginationDTO<ExerciseDTO> foundExercises = exerciseService.getExercisesByUser(1, 1);

        assertNotNull(foundExercises);
        assertEquals(exercises.size(), foundExercises.items().size());
        assertEquals(deadLiftExerciseDTO.id(), foundExercises.items().get(0).id());
        assertEquals(deadLiftExerciseDTO.exerciseName(), foundExercises.items().get(0).exerciseName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(exerciseRepository, times(1)).findByUser(page, user);
        verify(exerciseMapper, times(1)).toExerciseDTO(deadLiftExercise);
    }

    @Test
    public void getExercisesByUser_ShouldReturnAllExercises() {
        Pageable page = Pageable.unpaged();
        List<Exercise> exercises = List.of(squatExercise, deadLiftExercise);
        Page<Exercise> exercisesPage = new PageImpl<>(exercises, page, exercises.size());

        List<ExerciseDTO> exercisesDTO = List.of(squatExerciseDTO, deadLiftExerciseDTO);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(exerciseRepository.findByUser(page, user)).thenReturn(exercisesPage);
        when(exerciseMapper.toExerciseDTO(exercises.get(0))).thenReturn(exercisesDTO.get(0));
        when(exerciseMapper.toExerciseDTO(exercises.get(1))).thenReturn(exercisesDTO.get(1));

        PaginationDTO<ExerciseDTO> foundExercises = exerciseService.getExercisesByUser(-1, 1);

        assertNotNull(foundExercises);
        assertEquals(exercisesDTO.size(), foundExercises.items().size());
        assertEquals(exercisesDTO.get(0).id(), foundExercises.items().get(0).id());
        assertEquals(exercisesDTO.get(0).exerciseName(), foundExercises.items().get(0).exerciseName());
        assertEquals(exercisesDTO.get(1).id(), foundExercises.items().get(1).id());
        assertEquals(exercisesDTO.get(1).exerciseName(), foundExercises.items().get(1).exerciseName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(exerciseRepository, times(1)).findByUser(page, user);
        verify(exerciseMapper, times(2)).toExerciseDTO(any(Exercise.class));
    }

    @Test
    public void getExercisesNotInTrainingPlan_ShouldReturnExercisesNotInTrainingPlan() {
        Long trainingPlanId = 1L;
        List<Exercise> exercises = List.of(squatExercise);

        List<ExerciseDTO> exercisesDTO = List.of(squatExerciseDTO);

        TrainingPlan mockedTrainingPlan = new TrainingPlan();
        mockedTrainingPlan.setId(trainingPlanId);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanService.getTrainingPlanEntityById(trainingPlanId, user)).thenReturn(mockedTrainingPlan);

        when(exerciseRepository.findByUserAndTrainingPlansNotContaining(user, mockedTrainingPlan)).thenReturn(exercises);
        when(exerciseMapper.toExerciseDTO(exercises.get(0))).thenReturn(exercisesDTO.get(0));

        List<ExerciseDTO> exercisesNotInTrainingPlan = exerciseService.getExercisesNotInTrainingPlan(mockedTrainingPlan.getId());

        assertNotNull(exercisesNotInTrainingPlan);
        assertEquals(exercisesDTO.size(), exercisesNotInTrainingPlan.size());
        assertEquals(exercisesDTO.get(0).id(), exercisesNotInTrainingPlan.get(0).id());
        assertEquals(exercisesDTO.get(0).exerciseName(), exercisesNotInTrainingPlan.get(0).exerciseName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanService, times(1)).getTrainingPlanEntityById(trainingPlanId, user);
        verify(exerciseRepository, times(1)).findByUserAndTrainingPlansNotContaining(user, mockedTrainingPlan);
        verify(exerciseMapper, times(1)).toExerciseDTO(exercises.get(0));
    }

    @Test
    public void crateExercise_ShouldCreateExercise() {
        ExerciseRequest exerciseRequest = new ExerciseRequest("Squat");

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(squatExercise);
        when(exerciseMapper.toExerciseDTO(squatExercise)).thenReturn(squatExerciseDTO);

        ExerciseDTO savedExercise = exerciseService.createExercise(exerciseRequest);

        assertNotNull(savedExercise);
        assertEquals(exerciseRequest.exerciseName(), savedExercise.exerciseName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
        verify(exerciseMapper, times(1)).toExerciseDTO(squatExercise);
    }

    @Test
    public void deleteExerciseById_ShouldDeleteExercise() {
        TrainingPlan trainingPlan1 = TrainingPlan.builder().id(1L).exercises(new HashSet<>()).build();
        TrainingPlan trainingPlan2 = TrainingPlan.builder().id(2L).exercises(new HashSet<>()).build();

        squatExercise.setTrainingPlans(Set.of(trainingPlan1, trainingPlan2));

        trainingPlan1.getExercises().add(squatExercise);
        trainingPlan2.getExercises().add(squatExercise);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(exerciseRepository.findByIdAndUser(squatExerciseId, user)).thenReturn(Optional.of(squatExercise));
        doNothing().when(exerciseRepository).delete(squatExercise);

        exerciseService.deleteExerciseById(squatExerciseId);

        assertTrue(trainingPlan1.getExercises().isEmpty());
        assertTrue(trainingPlan2.getExercises().isEmpty());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(exerciseRepository, times(1)).findByIdAndUser(squatExerciseId, user);
        verify(exerciseRepository, times(1)).delete(squatExercise);
    }

    @Test
    public void changeExerciseName_ShouldChangeExerciseName() {
        ExerciseRequest exerciseRequest = new ExerciseRequest("Legpress");
        ExerciseDTO updatedExerciseDTO = new ExerciseDTO(squatExerciseId, "Legpress");


        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(exerciseRepository.findByIdAndUser(squatExerciseId, user)).thenReturn(Optional.of(squatExercise));
        when(exerciseRepository.save(any(Exercise.class))).thenReturn(squatExercise);
        when(exerciseMapper.toExerciseDTO(squatExercise)).thenReturn(updatedExerciseDTO);

        ExerciseDTO resultExercise = exerciseService.changeExerciseName(squatExerciseId, exerciseRequest);

        assertEquals(updatedExerciseDTO.id(), resultExercise.id());
        assertEquals(updatedExerciseDTO.exerciseName(), resultExercise.exerciseName());

        verify(userContext).getAuthenticatedUser();
        verify(exerciseRepository).findByIdAndUser(squatExercise.getId(), user);
        verify(exerciseRepository).save(squatExercise);
        verify(exerciseMapper).toExerciseDTO(squatExercise);
    }
}