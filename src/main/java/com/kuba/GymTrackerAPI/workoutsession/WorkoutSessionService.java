package com.kuba.GymTrackerAPI.workoutsession;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.security.UserContext;
import com.kuba.GymTrackerAPI.trainingplan.TrainingPlan;
import com.kuba.GymTrackerAPI.trainingplan.TrainingPlanService;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutsessionexercise.WorkoutSessionExercise;
import com.kuba.GymTrackerAPI.workoutsessionexercise.WorkoutSessionExerciseService;
import com.kuba.GymTrackerAPI.workoutsessionexerciseset.WorkoutSessionExerciseSet;
import com.kuba.GymTrackerAPI.workoutsessionexerciseset.WorkoutSessionExerciseSetRequestDTO;
import com.kuba.GymTrackerAPI.workoutsessionexerciseset.WorkoutSessionExerciseSetService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final UserContext userContext;

    private final WorkoutSessionRepository workoutSessionRepository;

    private final TrainingPlanService trainingPlanService;

    private final WorkoutSessionExerciseService workoutSessionExerciseService;

    private final WorkoutSessionExerciseSetService workoutSessionExerciseSetService;

    private final WorkoutSessionMapper workoutSessionMapper;

    public WorkoutSession getWorkoutSessionEntityById(Long workoutSessionId, User user) {
        log.info(
                "[METHOD]: getWorkoutSessionEntityById - Fetching workoutSession by ID: {} and user_id: {}",
                workoutSessionId,
                user.getId()
        );

        return workoutSessionRepository.findByIdAndUser(workoutSessionId, user).orElseThrow(() -> {
            log.warn(
                    "[METHOD]: getWorkoutSessionEntityById - workoutSession was not found by ID: {} and user_id: {}",
                    workoutSessionId,
                    user.getId()
            );

            return new NotFoundException("Trénink nenalezen!");
        });
    }

    public List<WorkoutSessionDTO> getWorkoutSessionsByUser() {
        User user = userContext.getAuthenticatedUser();

        log.info("[METHOD]: getWorkoutSessionsByUser - Fetching workout sessions for user_id: {}", user.getId());

        List<WorkoutSession> workoutSessions = workoutSessionRepository.findByUser(user);

        return workoutSessions
                .stream()
                .map(workoutSessionMapper::toWorkoutSessionDTO)
                .toList();
    }

    public WorkoutSessionDTO getWorkoutSessionById(Long id) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: getWorkoutSessionById - Fetching workoutSession by ID: {} for user_id: {}",
                id,
                user.getId()
        );

        WorkoutSession workoutSession = getWorkoutSessionEntityById(id, user);

        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }

    @Transactional
    public WorkoutSessionDTO createWorkoutSession(WorkoutSessionRequestDTO workoutSessionRequest) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: createWorkoutSession - Creating workout session with workoutSessionRequest: {} for user_id: {}",
                workoutSessionRequest,
                user.getId()
        );

        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanEntityById(
                workoutSessionRequest.trainingPlanId(),
                user
        );

        WorkoutSession workoutSession = WorkoutSession.builder()
                                                      .date(workoutSessionRequest.date())
                                                      .user(user)
                                                      .trainingPlan(trainingPlan)
                                                      .workoutSessionExercises(new ArrayList<>())
                                                      .build();

        for (Exercise exercise : trainingPlan.getExercises()) {
            WorkoutSessionExercise workoutSessionExercise = WorkoutSessionExercise.builder()
                                                                                  .workoutSession(workoutSession)
                                                                                  .exercise(exercise)
                                                                                  .workoutSessionExerciseSets(new ArrayList<>())
                                                                                  .build();

            log.info(
                    "[METHOD]: createWorkoutSession - Creating workoutSessionExercise for exercise by ID: {} in workoutSession",
                    exercise.getId()
            );

            exercise.getWorkoutSessionExercises().add(workoutSessionExercise);
            workoutSession.getWorkoutSessionExercises().add(workoutSessionExercise);
        }

        workoutSession = workoutSessionRepository.save(workoutSession);

        log.info("[METHOD]: createWorkoutSession - workoutSession created with ID: {}", workoutSession.getId());

        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }

    @Transactional
    public void deleteWorkoutSessionById(Long id) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: deleteWorkoutSessionById - Deleting workoutSession by ID: {} and user_id: {}",
                id,
                user.getId()
        );

        getWorkoutSessionEntityById(id, user);

        workoutSessionRepository.deleteById(id);

        log.info("[METHOD]: deleteWorkoutSessionById - workoutSession by ID: {} was deleted", id);
    }

    @Transactional
    public WorkoutSessionDTO createExerciseSet(
            Long workoutSessionId,
            Long workoutSessionExerciseId,
            WorkoutSessionExerciseSetRequestDTO workoutSessionExerciseSetRequest
    ) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: createExerciseSet - Creating exerciseSet for workoutSessionId: {} and workoutSessionExerciseId: {} with workoutSessionExerciseSetRequest: {}",
                workoutSessionId,
                workoutSessionExerciseId,
                workoutSessionExerciseSetRequest
        );

        WorkoutSession workoutSession = getWorkoutSessionEntityById(workoutSessionId, user);

        List<WorkoutSessionExercise> workoutSessionExercises = workoutSession.getWorkoutSessionExercises()
                                                                             .stream()
                                                                             .filter(wse -> wse.getId().equals(workoutSessionExerciseId))
                                                                             .toList();

        if (workoutSessionExercises.isEmpty()) {
            log.warn(
                    "[METHOD]: createExerciseSet - workoutSessionExercise by ID: {} wasn't found in workoutSession by ID: {}",
                    workoutSessionExerciseId,
                    workoutSessionId
            );

            throw new NotFoundException("Cvik v tréninku nenalezen!");
        }

        WorkoutSessionExerciseSet workoutExerciseSet = WorkoutSessionExerciseSet.builder()
                                                                                .workoutSessionExercise(workoutSessionExercises.get(0))
                                                                                .repetitions(workoutSessionExerciseSetRequest.repetitions())
                                                                                .weight(workoutSessionExerciseSetRequest.weight())
                                                                                .build();

        workoutSessionExercises.get(0).getWorkoutSessionExerciseSets().add(workoutExerciseSet);

        log.info(
                "[METHOD]: createExerciseSet - Saving workoutSessionExercise in workoutSession by ID: {}",
                workoutSessionId
        );

        workoutSession = workoutSessionRepository.save(workoutSession);

        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }

    @Transactional
    public void deleteExerciseSetById(
            Long workoutSessionId,
            Long workoutSessionExerciseId,
            Long workoutSessionExerciseSetId
    ) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: deleteExerciseSetById - Deleting workoutSessionExerciseSet by ID: {} - workoutSessionExercise by ID: {}  and workoutSession by ID: {}",
                workoutSessionExerciseSetId,
                workoutSessionExerciseId,
                workoutSessionId
        );

        WorkoutSession workoutSession = getWorkoutSessionEntityById(workoutSessionId, user);
        workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(
                workoutSessionExerciseId,
                workoutSession
        );
        workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId);

        workoutSessionExerciseSetService.deleteWorkoutSessionExerciseSetById(workoutSessionExerciseSetId);
    }

    @Transactional
    public WorkoutSessionDTO editExerciseSet(
            Long workoutSessionId,
            Long workoutSessionExerciseId,
            Long workoutSessionExerciseSetId,
            WorkoutSessionExerciseSetRequestDTO workoutSessionExerciseSetRequest
    ) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: editExerciseSet - Editing workoutSessionExerciseSet by ID: {} - workoutSessionExercise by ID: {} and workoutSession by ID: {} with workoutSessionExerciseSetRequest: {}",
                workoutSessionExerciseSetId,
                workoutSessionExerciseId,
                workoutSessionId,
                workoutSessionExerciseSetRequest
        );

        WorkoutSession workoutSession = getWorkoutSessionEntityById(workoutSessionId, user);
        workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(
                workoutSessionExerciseId,
                workoutSession
        );
        WorkoutSessionExerciseSet exerciseSet = workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId);

        exerciseSet.setRepetitions(workoutSessionExerciseSetRequest.repetitions());
        exerciseSet.setWeight(workoutSessionExerciseSetRequest.weight());

        log.info(
                "[METHOD]: editExerciseSet - Saving updated workoutSessionExerciseSet by ID: {}",
                workoutSessionExerciseSetId
        );

        workoutSessionExerciseSetService.saveWorkoutSessionExerciseSet(exerciseSet);

        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }
}
