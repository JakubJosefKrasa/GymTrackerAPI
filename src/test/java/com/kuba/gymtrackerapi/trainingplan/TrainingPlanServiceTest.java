package com.kuba.gymtrackerapi.trainingplan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kuba.gymtrackerapi.exceptions.BadRequestException;
import com.kuba.gymtrackerapi.exceptions.NotFoundException;
import com.kuba.gymtrackerapi.exercise.Exercise;
import com.kuba.gymtrackerapi.exercise.dto.ExerciseDTO;
import com.kuba.gymtrackerapi.exercise.ExerciseService;
import com.kuba.gymtrackerapi.pagination.PaginationDTO;
import com.kuba.gymtrackerapi.security.UserContext;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanDTO;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanExercisesDTO;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanRequestDTO;
import com.kuba.gymtrackerapi.user.User;
import com.kuba.gymtrackerapi.workoutsession.WorkoutSession;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    private TrainingPlanExercisesDTO legsTrainingPlanExercisesDTO;
    private TrainingPlanExercisesDTO pullTrainingPlanExercisesDTO;
    private TrainingPlanDTO legsTrainingPlanDTO;
    private TrainingPlanDTO pullTrainingPlanDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        legsTrainingPlan = TrainingPlan.builder().id(legsTrainingPlanId).trainingPlanName("Legs").user(user).exercises(new ArrayList<>()).workoutSessions(new HashSet<>()).build();
        legsTrainingPlanExercisesDTO = new TrainingPlanExercisesDTO(legsTrainingPlanId, "Legs", new ArrayList<>());
        legsTrainingPlanDTO = new TrainingPlanDTO(legsTrainingPlanId, "Legs");

        pullTrainingPlan = TrainingPlan.builder().id(pullTrainingPlanId).trainingPlanName("Pull").user(user).build();
        pullTrainingPlanExercisesDTO = new TrainingPlanExercisesDTO(pullTrainingPlanId, "Pull", new ArrayList<>());
        pullTrainingPlanDTO = new TrainingPlanDTO(pullTrainingPlanId, "Pull");
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
        when(trainingPlanMapper.toTrainingPlanDTO(pullTrainingPlan)).thenReturn(pullTrainingPlanDTO);

        PaginationDTO<TrainingPlanDTO> foundTrainingPlans = trainingPlanService.getTrainingPlansByUser(1, 1);

        assertNotNull(foundTrainingPlans);
        assertEquals(trainingPlans.size(), foundTrainingPlans.items().size());
        assertEquals(pullTrainingPlanDTO.id(), foundTrainingPlans.items().get(0).id());
        assertEquals(pullTrainingPlanDTO.trainingPlanName(), foundTrainingPlans.items().get(0).trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByUser(page, user);
        verify(trainingPlanMapper, times(1)).toTrainingPlanDTO(pullTrainingPlan);
    }

    @Test
    public void getTrainingPlansByUser_ShouldReturnAllTrainingPlans() {
        Pageable page = Pageable.unpaged();
        List<TrainingPlan> trainingPlans = List.of(legsTrainingPlan, pullTrainingPlan);
        PageImpl<TrainingPlan> trainingPlansPage = new PageImpl<>(trainingPlans, page, trainingPlans.size());

        List<TrainingPlanDTO> trainingPlansDTO = List.of(
                legsTrainingPlanDTO,
                pullTrainingPlanDTO
        );

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByUser(page, user)).thenReturn(trainingPlansPage);
        when(trainingPlanMapper.toTrainingPlanDTO(trainingPlans.get(0))).thenReturn(trainingPlansDTO.get(0));
        when(trainingPlanMapper.toTrainingPlanDTO(trainingPlans.get(1))).thenReturn(trainingPlansDTO.get(1));

        PaginationDTO<TrainingPlanDTO> foundTrainingPlans = trainingPlanService.getTrainingPlansByUser(-1, 1);

        assertNotNull(foundTrainingPlans);
        assertEquals(trainingPlansDTO.size(), foundTrainingPlans.items().size());
        assertEquals(trainingPlansDTO.get(0).id(), foundTrainingPlans.items().get(0).id());
        assertEquals(trainingPlansDTO.get(0).trainingPlanName(), foundTrainingPlans.items().get(0).trainingPlanName());
        assertEquals(trainingPlansDTO.get(1).id(), foundTrainingPlans.items().get(1).id());
        assertEquals(trainingPlansDTO.get(1).trainingPlanName(), foundTrainingPlans.items().get(1).trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByUser(page, user);
        verify(trainingPlanMapper, times(2)).toTrainingPlanDTO(any(TrainingPlan.class));
    }

    @Test
    public void getTrainingPlanById_ShouldReturnTrainingPlan() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findWithExercisesByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(legsTrainingPlan)).thenReturn(legsTrainingPlanExercisesDTO);

        TrainingPlanExercisesDTO foundTrainingPlan = trainingPlanService.getTrainingPlanById(legsTrainingPlanId);

        assertNotNull(foundTrainingPlan);
        assertEquals(legsTrainingPlanExercisesDTO.id(), foundTrainingPlan.id());
        assertEquals(legsTrainingPlanExercisesDTO.trainingPlanName(), foundTrainingPlan.trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findWithExercisesByIdAndUser(legsTrainingPlanId, user);
        verify(trainingPlanMapper, times(1)).toTrainingPlanExercisesDTO(legsTrainingPlan);
    }

    @Test
    public void createTrainingPlan_ShouldCreateTrainingPlan() {
        TrainingPlanRequestDTO trainingPlanRequest = new TrainingPlanRequestDTO("Legs");

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.save(any(TrainingPlan.class))).thenReturn(legsTrainingPlan);
        when(trainingPlanMapper.toTrainingPlanDTO(legsTrainingPlan)).thenReturn(legsTrainingPlanDTO);

        TrainingPlanDTO createdTrainingPlan = trainingPlanService.createTrainingPlan(trainingPlanRequest);

        assertNotNull(createdTrainingPlan);
        assertEquals(legsTrainingPlan.getId(), createdTrainingPlan.id());
        assertEquals(legsTrainingPlan.getTrainingPlanName(), createdTrainingPlan.trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).save(any(TrainingPlan.class));
        verify(trainingPlanMapper, times(1)).toTrainingPlanDTO(legsTrainingPlan);
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
        TrainingPlanDTO updatedTrainingPlanDTO = new TrainingPlanDTO(legsTrainingPlanId, "Push");

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(trainingPlanRepository.save(legsTrainingPlan)).thenReturn(updatedTrainingPlan);
        when(trainingPlanMapper.toTrainingPlanDTO(updatedTrainingPlan)).thenReturn(updatedTrainingPlanDTO);

        TrainingPlanDTO changedTrainingPlan = trainingPlanService.changeTrainingPlanName(legsTrainingPlanId, trainingPlanRequest);

        assertEquals(trainingPlanRequest.trainingPlanName(), changedTrainingPlan.trainingPlanName());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findByIdAndUser(legsTrainingPlanId, user);
        verify(trainingPlanRepository, times(1)).save(legsTrainingPlan);
        verify(trainingPlanMapper, times(1)).toTrainingPlanDTO(updatedTrainingPlan);
    }

    @Test
    public void addExerciseInTrainingPlan_ShouldThrowBadRequestException() {
        Long exerciseId = 1L;
        Exercise exercise = Exercise.builder().id(exerciseId).exerciseName("Squat").user(user).trainingPlans(new HashSet<>()).build();
        legsTrainingPlan.getExercises().add(exercise);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findWithExercisesWorkoutSessionsByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(exerciseService.getExerciseEntityById(exerciseId, user)).thenReturn(exercise);

        assertThrows(BadRequestException.class, () -> trainingPlanService.addExerciseInTrainingPlan(legsTrainingPlanId, exerciseId));
    }

    @Test
    public void addExerciseInTrainingPlan_ShouldAddExerciseInTrainingPlan() {
        Long exerciseId = 1L;
        Exercise exercise = Exercise.builder().id(exerciseId).exerciseName("Squat").user(user).trainingPlans(new HashSet<>()).build();
        WorkoutSession workoutSession = WorkoutSession.builder().id(1L).trainingPlan(legsTrainingPlan).workoutSessionExercises(new HashSet<>()).build();
        legsTrainingPlan.getWorkoutSessions().add(workoutSession);
        TrainingPlanExercisesDTO trainingPlanExercisesDTO = new TrainingPlanExercisesDTO(1L, "Legs", new ArrayList<>(List.of(new ExerciseDTO(legsTrainingPlanId, "Squat"))));

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanRepository.findWithExercisesWorkoutSessionsByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(exerciseService.getExerciseEntityById(exerciseId, user)).thenReturn(exercise);
        when(trainingPlanRepository.save(legsTrainingPlan)).thenReturn(legsTrainingPlan);
        when(trainingPlanMapper.toTrainingPlanExercisesDTO(legsTrainingPlan)).thenReturn(trainingPlanExercisesDTO);

        trainingPlanService.addExerciseInTrainingPlan(legsTrainingPlanId, exerciseId);

        assertTrue(legsTrainingPlan.getExercises().contains(exercise));
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findWithExercisesWorkoutSessionsByIdAndUser(legsTrainingPlanId, user);
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
        when(trainingPlanRepository.findWithExercisesByIdAndUser(legsTrainingPlanId, user)).thenReturn(Optional.of(legsTrainingPlan));
        when(exerciseService.getExerciseEntityById(exerciseId, user)).thenReturn(exercise);
        when(trainingPlanRepository.save(legsTrainingPlan)).thenReturn(legsTrainingPlan);

        trainingPlanService.removeExerciseFromTrainingPlan(legsTrainingPlanId, exerciseId);

        assertFalse(legsTrainingPlan.getExercises().contains(exercise));
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanRepository, times(1)).findWithExercisesByIdAndUser(legsTrainingPlanId, user);
        verify(exerciseService, times(1)).getExerciseEntityById(exerciseId, user);
        verify(trainingPlanRepository, times(1)).save(legsTrainingPlan);
    }
}