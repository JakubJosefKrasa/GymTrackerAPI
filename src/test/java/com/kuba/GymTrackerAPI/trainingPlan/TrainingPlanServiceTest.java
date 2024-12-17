package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exceptions.BadRequestException;
import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.exercise.ExerciseDTO;
import com.kuba.GymTrackerAPI.exercise.ExerciseService;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.security.UserContext;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSession.WorkoutSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingPlanServiceTest {

    @Mock
    private UserContext userContext;
    @Mock
    private TrainingPlanRepository trainingPlanRepository;
    @Mock
    private ExerciseService exerciseService;
    @Mock
    private TrainingPlanMapper trainingPlanMapper;
    @InjectMocks
    private TrainingPlanService trainingPlanService;

    private User user;
    private final Long legsTrainingPlanId = 1L;
    private final Long pullTrainingPlanId = 2L;
    private TrainingPlan legsTrainingPlan;
    private TrainingPlan pullTrainingPlan;
    private TrainingPlanExercisesDTO legsTrainingPlanDTO;
    private TrainingPlanExercisesDTO pullTrainingPlanDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        legsTrainingPlan = TrainingPlan.builder().id(legsTrainingPlanId).trainingPlanName("Legs").user(user).exercises(new HashSet<>()).workoutSessions(new ArrayList<>()).build();
        legsTrainingPlanDTO = new TrainingPlanExercisesDTO(legsTrainingPlanId, "Legs", new HashSet<>());

        pullTrainingPlan = TrainingPlan.builder().id(pullTrainingPlanId).trainingPlanName("Pull").user(user).build();
        pullTrainingPlanDTO = new TrainingPlanExercisesDTO(pullTrainingPlanId, "Pull", new HashSet<>());
    }

    @Test
    public void getTrainingPlanEntityById_ShouldReturnTrainingPlan() {
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));

        TrainingPlan foundTrainingPlan = trainingPlanService.getTrainingPlanEntityById(legsTrainingPlanId, user);

        assertNotNull(foundTrainingPlan);
        assertEquals(legsTrainingPlan.getId(), foundTrainingPlan.getId());
        assertEquals(legsTrainingPlan.getTrainingPlanName(), foundTrainingPlan.getTrainingPlanName());
        verify(trainingPlanRepository, times(1)).findByIdAndUser(legsTrainingPlanId, user);
    }

    @Test
    public void getTrainingPlanEntityById_ShouldThrowNotFoundException() {
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> trainingPlanService.getTrainingPlanEntityById(legsTrainingPlanId, user));
        verify(trainingPlanRepository, times(1)).findByIdAndUser(legsTrainingPlanId, user);
    }

    @Test
    public void getTrainingPlansByUser_ShouldReturnPagedTrainingPlan() {
        PageRequest page = PageRequest.of(1, 1);
        List<TrainingPlan> trainingPlans = List.of(pullTrainingPlan);
        PageImpl<TrainingPlan> trainingPlansPage = new PageImpl<>(trainingPlans, page, trainingPlans.size());

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByUser(page, user)).thenReturn(trainingPlansPage);
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(pullTrainingPlan)).thenReturn(pullTrainingPlanDTO);

        PaginationDTO<TrainingPlanExercisesDTO> foundTrainingPlans = trainingPlanService.getTrainingPlansByUser(1, 1);

        assertNotNull(foundTrainingPlans);
        assertEquals(trainingPlans.size(), foundTrainingPlans.items().size());
        assertEquals(pullTrainingPlanDTO.id(), foundTrainingPlans.items().get(0).id());
        assertEquals(pullTrainingPlanDTO.trainingPlanName(), foundTrainingPlans.items().get(0).trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByUser(page, user);
        verify(trainingPlanMapper, times(1)).toTrainingPlanExercisesDTO(pullTrainingPlan);
    }

    @Test
    public void getTrainingPlansByUser_ShouldReturnAllTrainingPlans() {
        Pageable page = Pageable.unpaged();
        List<TrainingPlan> trainingPlans = List.of(legsTrainingPlan, pullTrainingPlan);
        PageImpl<TrainingPlan> trainingPlansPage = new PageImpl<>(trainingPlans, page, trainingPlans.size());

        List<TrainingPlanExercisesDTO> trainingPlansDTO = List.of(legsTrainingPlanDTO, pullTrainingPlanDTO);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByUser(page, user)).thenReturn(trainingPlansPage);
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlans.get(0))).thenReturn(trainingPlansDTO.get(0));
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlans.get(1))).thenReturn(trainingPlansDTO.get(1));

        PaginationDTO<TrainingPlanExercisesDTO> foundTrainingPlans = trainingPlanService.getTrainingPlansByUser(-1, 1);

        assertNotNull(foundTrainingPlans);
        assertEquals(trainingPlansDTO.size(), foundTrainingPlans.items().size());
        assertEquals(trainingPlansDTO.get(0).id(), foundTrainingPlans.items().get(0).id());
        assertEquals(trainingPlansDTO.get(0).trainingPlanName(), foundTrainingPlans.items().get(0).trainingPlanName());
        assertEquals(trainingPlansDTO.get(1).id(), foundTrainingPlans.items().get(1).id());
        assertEquals(trainingPlansDTO.get(1).trainingPlanName(), foundTrainingPlans.items().get(1).trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByUser(page, user);
        verify(trainingPlanMapper, times(2)).toTrainingPlanExercisesDTO(any(TrainingPlan.class));
    }

    @Test
    public void getTrainingPlanById_ShouldReturnTrainingPlan() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(legsTrainingPlan)).thenReturn(legsTrainingPlanDTO);

        TrainingPlanExercisesDTO foundTrainingPlan = trainingPlanService.getTrainingPlanById(legsTrainingPlanId);

        assertNotNull(foundTrainingPlan);
        assertEquals(legsTrainingPlanDTO.id(), foundTrainingPlan.id());
        assertEquals(legsTrainingPlanDTO.trainingPlanName(), foundTrainingPlan.trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByIdAndUser(legsTrainingPlanId, user);
        verify(trainingPlanMapper, times(1)).toTrainingPlanExercisesDTO(legsTrainingPlan);
    }

    @Test
    public void createTrainingPlan_ShouldCreateTrainingPlan() {
        TrainingPlanRequestDTO trainingPlanRequest = new TrainingPlanRequestDTO("Legs");

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.save(any(TrainingPlan.class))).thenReturn(legsTrainingPlan);
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(legsTrainingPlan)).thenReturn(legsTrainingPlanDTO);

        TrainingPlanExercisesDTO createdTrainingPlan = trainingPlanService.createTrainingPlan(trainingPlanRequest);

        assertNotNull(createdTrainingPlan);
        assertEquals(legsTrainingPlan.getId(), createdTrainingPlan.id());
        assertEquals(legsTrainingPlan.getTrainingPlanName(), createdTrainingPlan.trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).save(any(TrainingPlan.class));
        verify(trainingPlanMapper, times(1)).toTrainingPlanExercisesDTO(legsTrainingPlan);
    }

    @Test
    public void deleteTrainingPlanById_ShouldDeleteTrainingPlan() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        doNothing().when(trainingPlanRepository).delete(legsTrainingPlan);

        trainingPlanService.deleteTrainingPlanById(legsTrainingPlanId);

        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).delete(legsTrainingPlan);
    }

    @Test
    public void changeTrainingPlanName_ShouldChangeTrainingPlanName() {
        TrainingPlanRequestDTO trainingPlanRequest = new TrainingPlanRequestDTO("Push");

        TrainingPlan updatedTrainingPlan = TrainingPlan.builder().id(legsTrainingPlanId).trainingPlanName("Push").user(user).build();
        TrainingPlanExercisesDTO updatedTrainingPlanDTO = new TrainingPlanExercisesDTO(legsTrainingPlanId, "Push", new HashSet<>(Set.of()));

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(trainingPlanRepository.save(legsTrainingPlan)).thenReturn(updatedTrainingPlan);
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(updatedTrainingPlan)).thenReturn(updatedTrainingPlanDTO);

        TrainingPlanExercisesDTO changedTrainingPlan = trainingPlanService.changeTrainingPlanName(legsTrainingPlanId, trainingPlanRequest);

        assertEquals(trainingPlanRequest.trainingPlanName(), changedTrainingPlan.trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByIdAndUser(legsTrainingPlanId, user);
        verify(trainingPlanRepository, times(1)).save(legsTrainingPlan);
        verify(trainingPlanMapper, times(1)).toTrainingPlanExercisesDTO(updatedTrainingPlan);
    }

    @Test
    public void addExerciseInTrainingPlan_ShouldThrowBadRequestException() {
        Long exerciseId = 1L;
        Exercise exercise = Exercise.builder().id(exerciseId).exerciseName("Squat").user(user).trainingPlans(new HashSet<>()).build();
        legsTrainingPlan.getExercises().add(exercise);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(exerciseService.getExerciseEntityById(exerciseId, user)).thenReturn(exercise);

        assertThrows(BadRequestException.class, () -> trainingPlanService.addExerciseInTrainingPlan(legsTrainingPlanId, exerciseId));
    }

    @Test
    public void addExerciseInTrainingPlan_ShouldAddExerciseInTrainingPlan() {
        Long exerciseId = 1L;
        Exercise exercise = Exercise.builder().id(exerciseId).exerciseName("Squat").user(user).trainingPlans(new HashSet<>()).build();
        WorkoutSession workoutSession = WorkoutSession.builder().id(1L).trainingPlan(legsTrainingPlan).workoutSessionExercises(new ArrayList<>()).build();
        legsTrainingPlan.getWorkoutSessions().add(workoutSession);
        TrainingPlanExercisesDTO trainingPlanExercisesDTO = new TrainingPlanExercisesDTO(1L, "Legs", new HashSet<>(Set.of(new ExerciseDTO(legsTrainingPlanId, "Squat"))));

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(exerciseService.getExerciseEntityById(exerciseId, user)).thenReturn(exercise);
        when(trainingPlanRepository.save(legsTrainingPlan)).thenReturn(legsTrainingPlan);
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(legsTrainingPlan)).thenReturn(trainingPlanExercisesDTO);

        trainingPlanService.addExerciseInTrainingPlan(legsTrainingPlanId, exerciseId);

        assertTrue(legsTrainingPlan.getExercises().contains(exercise));
        assertEquals(1, legsTrainingPlan.getWorkoutSessions().get(0).getWorkoutSessionExercises().size());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByIdAndUser(legsTrainingPlanId, user);
        verify(exerciseService, times(1)).getExerciseEntityById(exerciseId, user);
        verify(trainingPlanRepository, times(1)).save(legsTrainingPlan);
        verify(trainingPlanMapper, times(1)).toTrainingPlanExercisesDTO(legsTrainingPlan);
    }

    @Test
    public void removeExerciseFromTrainingPlan_ShouldRemoveExerciseFromTrainingPlan() {
        Long exerciseId = 1L;
        Exercise exercise = Exercise.builder().id(exerciseId).exerciseName("Squat").user(user).build();
        legsTrainingPlan.getExercises().add(exercise);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(exerciseService.getExerciseEntityById(exerciseId, user)).thenReturn(exercise);
        when(trainingPlanRepository.save(legsTrainingPlan)).thenReturn(legsTrainingPlan);

        trainingPlanService.removeExerciseFromTrainingPlan(legsTrainingPlanId, exerciseId);

        assertFalse(legsTrainingPlan.getExercises().contains(exercise));
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByIdAndUser(legsTrainingPlanId, user);
        verify(exerciseService, times(1)).getExerciseEntityById(exerciseId, user);
        verify(trainingPlanRepository, times(1)).save(legsTrainingPlan);
    }
}