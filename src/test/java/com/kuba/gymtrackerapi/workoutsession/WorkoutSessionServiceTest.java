package com.kuba.gymtrackerapi.workoutsession;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kuba.gymtrackerapi.exceptions.NotFoundException;
import com.kuba.gymtrackerapi.exercise.Exercise;
import com.kuba.gymtrackerapi.exercise.dto.ExerciseSetDTO;
import com.kuba.gymtrackerapi.security.UserContext;
import com.kuba.gymtrackerapi.trainingplan.TrainingPlan;
import com.kuba.gymtrackerapi.trainingplan.TrainingPlanService;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanDTO;
import com.kuba.gymtrackerapi.trainingplan.dto.TrainingPlanWorkoutSessionExercisesDTO;
import com.kuba.gymtrackerapi.user.User;
import com.kuba.gymtrackerapi.workoutsession.dto.WorkoutSessionDTO;
import com.kuba.gymtrackerapi.workoutsession.dto.WorkoutSessionExercisesDTO;
import com.kuba.gymtrackerapi.workoutsession.dto.WorkoutSessionRequestDTO;
import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExercise;
import com.kuba.gymtrackerapi.workoutsessionexercise.dto.WorkoutSessionExerciseDTO;
import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExerciseService;
import com.kuba.gymtrackerapi.workoutsessionexerciseset.dto.WorkoutSessionExerciseSetDTO;
import com.kuba.gymtrackerapi.workoutsessionexerciseset.WorkoutSessionExerciseSet;
import com.kuba.gymtrackerapi.workoutsessionexerciseset.dto.WorkoutSessionExerciseSetRequestDTO;
import com.kuba.gymtrackerapi.workoutsessionexerciseset.WorkoutSessionExerciseSetService;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkoutSessionServiceTest {

    @Mock
    private UserContext userContext;
    @Mock
    private WorkoutSessionRepository workoutSessionRepository;
    @Mock
    private TrainingPlanService trainingPlanService;
    @Mock
    private WorkoutSessionExerciseService workoutSessionExerciseService;
    @Mock
    private WorkoutSessionExerciseSetService workoutSessionExerciseSetService;
    @Mock
    private WorkoutSessionMapper workoutSessionMapper;

    @InjectMocks
    private WorkoutSessionService workoutSessionService;

    private User user;

    private final Long squatSetId = 1L;
    private final Long squatExerciseId = 1L;
    private final Long squatWorkoutSessionExerciseId = 1L;
    private final Long trainingPlanId = 1L;
    private final Long workoutSessionId = 1L;
    private WorkoutSessionExerciseSet squatSet;
    private WorkoutSession workoutSession;
    private WorkoutSessionExercisesDTO workoutSessionExercisesDTO;
    private WorkoutSessionDTO workoutSessionDTO;

    @BeforeEach
    void setUp() {
        squatSet = WorkoutSessionExerciseSet.builder().id(squatSetId).repetitions(10).weight(122.5f).build();
        WorkoutSessionExerciseSetDTO squatSetDTO = new WorkoutSessionExerciseSetDTO(squatSetId, 10, 122.5f);
        ExerciseSetDTO squatExerciseSetDTO = new ExerciseSetDTO(squatExerciseId, "Squat", new HashSet<>(Set.of(squatSetDTO)));
        WorkoutSessionExerciseDTO squatWorkoutSessionExerciseDTO = new WorkoutSessionExerciseDTO(squatWorkoutSessionExerciseId, squatExerciseSetDTO);
        TrainingPlanWorkoutSessionExercisesDTO legsTrainingPlanWorkoutSessionDTO = new TrainingPlanWorkoutSessionExercisesDTO(trainingPlanId, "Legs", new HashSet<>(Set.of(squatWorkoutSessionExerciseDTO)));
        WorkoutSessionExercise squatWorkoutSessionExercise = WorkoutSessionExercise.builder().id(squatExerciseId).workoutSessionExerciseSets(new HashSet<>()).build();
        TrainingPlanDTO trainingPlanDTO = new TrainingPlanDTO(trainingPlanId, "Legs");

        user = new User();
        workoutSession = WorkoutSession.builder().id(workoutSessionId).date(LocalDate.now()).user(user).workoutSessionExercises(new HashSet<>(Set.of(squatWorkoutSessionExercise))).build();
        workoutSessionExercisesDTO = new WorkoutSessionExercisesDTO(
                workoutSessionId,
                LocalDate.now(),
                legsTrainingPlanWorkoutSessionDTO
        );
        workoutSessionDTO = new WorkoutSessionDTO(workoutSessionId, LocalDate.now(), trainingPlanDTO);
    }

    @Test
    public void getWorkoutSessionEntityById_ShouldReturnWorkoutSessionWithTrainingPlanWorkoutSessionExercises() {
        when(workoutSessionRepository.findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user)).thenReturn(Optional.of(workoutSession));

        WorkoutSession foundWorkoutSession = workoutSessionService.getWorkoutSessionEntityWithTrainingPlanWorkoutSessionExercisesById(workoutSessionId, user);

        assertNotNull(foundWorkoutSession);
        assertEquals(workoutSession.getId(), foundWorkoutSession.getId());
        assertEquals(workoutSession.getDate(), foundWorkoutSession.getDate());
        verify(workoutSessionRepository, times(1)).findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user);
    }

    @Test
    public void getWorkoutSessionEntityWithTrainingPlanWorkoutSessionExercisesById_ShouldThrowNotFoundException() {
        when(workoutSessionRepository.findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> workoutSessionService.getWorkoutSessionEntityWithTrainingPlanWorkoutSessionExercisesById(workoutSessionId, user));
        verify(workoutSessionRepository, times(1)).findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user);
    }

    @Test
    public void getWorkoutSessionsByUser_ShouldReturnWorkoutSessions() {
        List<WorkoutSession> workoutSessions = List.of(workoutSession);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(workoutSessionRepository.findByUser(user)).thenReturn(workoutSessions);
        when(workoutSessionMapper.toWorkoutSessionDTO(workoutSessions.get(0))).thenReturn(workoutSessionDTO);

        List<WorkoutSessionDTO> foundWorkoutSessions = workoutSessionService.getWorkoutSessionsByUser();

        assertNotNull(foundWorkoutSessions);
        assertEquals(workoutSessions.size(), foundWorkoutSessions.size());
        assertEquals(workoutSessions.get(0).getId(), foundWorkoutSessions.get(0).id());

        verify(userContext, times(1)).getAuthenticatedUser();
        verify(workoutSessionRepository, times(1)).findByUser(user);
        verify(workoutSessionMapper, times(1)).toWorkoutSessionDTO(workoutSession);
    }

    @Test
    public void getWorkoutSessionById_ShouldReturnWorkoutSession() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(workoutSessionRepository.findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user)).thenReturn(Optional.of(workoutSession));
        when(workoutSessionMapper.toWorkoutSessionExercisesDTO(workoutSession)).thenReturn(workoutSessionExercisesDTO);

        WorkoutSessionExercisesDTO foundWorkoutSession = workoutSessionService.getWorkoutSessionById(workoutSessionId);

        assertNotNull(foundWorkoutSession);
        assertEquals(workoutSession.getId(), foundWorkoutSession.id());
        assertEquals(workoutSession.getDate(), foundWorkoutSession.date());

        verify(userContext, times(1)).getAuthenticatedUser();
        verify(workoutSessionRepository, times(1)).findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user);
        verify(workoutSessionMapper, times(1)).toWorkoutSessionExercisesDTO(workoutSession);
    }

    @Test
    public void createWorkoutSession_ShouldCreateWorkoutSession() {
        Long pullTrainingPlanId = 2L;
        WorkoutSessionRequestDTO workoutSessionRequest = new WorkoutSessionRequestDTO(LocalDate.now(), pullTrainingPlanId);

        Long deadLiftId = 1L;
        Long pulloverId = 2L;
        List<Exercise> exercises = List.of(
                Exercise.builder().id(deadLiftId).exerciseName("Deadlift").workoutSessionExercises(new LinkedHashSet<>()).build(),
                Exercise.builder().id(pulloverId).exerciseName("Pullover").workoutSessionExercises(new LinkedHashSet<>()).build()
        );
        TrainingPlan pullTrainingPlan = TrainingPlan.builder().id(pullTrainingPlanId).trainingPlanName("Pull").exercises(exercises).build();


        Set<WorkoutSessionExerciseDTO> wseDTO = Set.of(
                new WorkoutSessionExerciseDTO(deadLiftId, new ExerciseSetDTO(1L, "Deadlift", new HashSet<>())),
                new WorkoutSessionExerciseDTO(pulloverId, new ExerciseSetDTO(2L, "Pullover", new HashSet<>()))
        );
        TrainingPlanWorkoutSessionExercisesDTO tpwseDTO = new TrainingPlanWorkoutSessionExercisesDTO(pullTrainingPlanId, "Pull", wseDTO);
        WorkoutSessionExercisesDTO expectedCreatedWorkoutSessionExercisesDTO = new WorkoutSessionExercisesDTO(workoutSessionId, LocalDate.now(), tpwseDTO);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(trainingPlanService.getTrainingPlanEntityById(pullTrainingPlanId, user)).thenReturn(pullTrainingPlan);
        when(workoutSessionRepository.save(any(WorkoutSession.class))).thenReturn(workoutSession);
        when(workoutSessionMapper.toWorkoutSessionExercisesDTO(any(WorkoutSession.class))).thenReturn(
                expectedCreatedWorkoutSessionExercisesDTO);

        WorkoutSessionExercisesDTO createdWorkoutSessionExercisesDTO = workoutSessionService.createWorkoutSession(workoutSessionRequest);

        assertNotNull(createdWorkoutSessionExercisesDTO);
        assertEquals(expectedCreatedWorkoutSessionExercisesDTO.id(), createdWorkoutSessionExercisesDTO.id());
        assertEquals(expectedCreatedWorkoutSessionExercisesDTO.date(), createdWorkoutSessionExercisesDTO.date());
        assertEquals(expectedCreatedWorkoutSessionExercisesDTO.trainingPlan().workoutSessionExercises().size(), createdWorkoutSessionExercisesDTO.trainingPlan().workoutSessionExercises().size());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(trainingPlanService, times(1)).getTrainingPlanEntityById(pullTrainingPlanId, user);
        verify(workoutSessionRepository, times(1)).save(any(WorkoutSession.class));
        verify(workoutSessionMapper, times(1)).toWorkoutSessionExercisesDTO(any(WorkoutSession.class));
    }

    @Test
    public void deleteWorkoutSessionById_ShouldDeleteWorkoutSession() {
        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(workoutSessionRepository.findByIdAndUser(workoutSessionId, user)).thenReturn(Optional.of(workoutSession));
        doNothing().when(workoutSessionRepository).deleteById(workoutSessionId);

        workoutSessionService.deleteWorkoutSessionById(workoutSessionId);

        verify(userContext, times(1)).getAuthenticatedUser();
        verify(workoutSessionRepository, times(1)).findByIdAndUser(workoutSessionId, user);
        verify(workoutSessionRepository, times(1)).deleteById(workoutSessionId);
    }

    @Test
    public void createExerciseSet_ShouldThrowNotFoundException() {
        WorkoutSessionExerciseSetRequestDTO createRequest = new WorkoutSessionExerciseSetRequestDTO(8, 110);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(workoutSessionRepository.findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user)).thenReturn(Optional.of(workoutSession));

        assertThrows(NotFoundException.class, () -> workoutSessionService.createExerciseSet(workoutSessionId, 5L, createRequest));
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(workoutSessionRepository, times(1)).findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user);
    }

    @Test
    public void createExerciseSet_ShouldCreateExerciseSet() {
        WorkoutSessionExerciseSetRequestDTO createRequest = new WorkoutSessionExerciseSetRequestDTO(8, 110);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(workoutSessionRepository.findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user)).thenReturn(Optional.of(workoutSession));
        when(workoutSessionRepository.save(workoutSession)).thenReturn(workoutSession);
        when(workoutSessionMapper.toWorkoutSessionExercisesDTO(workoutSession)).thenReturn(workoutSessionExercisesDTO);

        WorkoutSessionExercisesDTO updatedWorkoutSession = workoutSessionService.createExerciseSet(workoutSessionId, squatWorkoutSessionExerciseId, createRequest);

        assertNotNull(updatedWorkoutSession);
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(workoutSessionRepository, times(1)).findWithTrainingPlanWorkoutSessionExercisesByIdAndUser(workoutSessionId, user);
        verify(workoutSessionRepository, times(1)).save(workoutSession);
        verify(workoutSessionMapper, times(1)).toWorkoutSessionExercisesDTO(workoutSession);
    }

    @Test
    public void deleteExerciseSetById_ShouldDeleteExerciseSet() {
        Long workoutSessionExerciseId = 1L;
        Long workoutSessionExerciseSetId = 1L;

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(workoutSessionRepository.findByIdAndUser(workoutSessionId, user)).thenReturn(Optional.of(workoutSession));
        when(workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession)).thenReturn(null);
        when(workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId)).thenReturn(null);
        doNothing().when(workoutSessionExerciseSetService).deleteWorkoutSessionExerciseSetById(workoutSessionExerciseSetId);

        workoutSessionService.deleteExerciseSetById(workoutSessionId, workoutSessionExerciseId, workoutSessionExerciseSetId);

        verify(userContext, times(1)).getAuthenticatedUser();
        verify(workoutSessionRepository, times(1)).findByIdAndUser(workoutSessionId, user);
        verify(workoutSessionExerciseService, times(1)).getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession);
        verify(workoutSessionExerciseSetService, times(1)).getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId);
        verify(workoutSessionExerciseSetService, times(1)).deleteWorkoutSessionExerciseSetById(workoutSessionExerciseSetId);
    }

    @Test
    public void editExerciseSet_ShouldEditExerciseSet() {
        WorkoutSessionExerciseSetRequestDTO editRequest = new WorkoutSessionExerciseSetRequestDTO(8, 110);

        when(userContext.getAuthenticatedUser()).thenReturn(user);
        when(workoutSessionRepository.findByIdAndUser(workoutSessionId, user)).thenReturn(Optional.of(workoutSession));
        when(workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionId, workoutSession)).thenReturn(null);
        when(workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(squatWorkoutSessionExerciseId)).thenReturn(squatSet);
        when(workoutSessionExerciseSetService.saveWorkoutSessionExerciseSet(squatSet)).thenReturn(null);
        when(workoutSessionMapper.toWorkoutSessionExercisesDTO(workoutSession)).thenReturn(workoutSessionExercisesDTO);

        workoutSessionService.editExerciseSet(workoutSessionId, squatWorkoutSessionExerciseId, squatSetId, editRequest);

        assertEquals(editRequest.repetitions(), squatSet.getRepetitions());
        assertEquals(editRequest.weight(), squatSet.getWeight());
        verify(userContext, times(1)).getAuthenticatedUser();
        verify(workoutSessionRepository, times(1)).findByIdAndUser(workoutSessionId, user);
        verify(workoutSessionExerciseService, times(1)).getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionId, workoutSession);
        verify(workoutSessionExerciseSetService, times(1)).getWorkoutSessionExerciseSetEntityById(squatWorkoutSessionExerciseId);
        verify(workoutSessionExerciseSetService, times(1)).saveWorkoutSessionExerciseSet(squatSet);
        verify(workoutSessionMapper, times(1)).toWorkoutSessionExercisesDTO(workoutSession);
    }
}