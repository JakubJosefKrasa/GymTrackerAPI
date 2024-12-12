package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.security.UserContext;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlan;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlanService;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExerciseService;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSet;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetRequest;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutSessionService {
    private final UserContext userContext;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final TrainingPlanService trainingPlanService;
    private final WorkoutSessionExerciseService workoutSessionExerciseService;
    private final WorkoutSessionExerciseSetService workoutSessionExerciseSetService;
    private final WorkoutSessionMapper workoutSessionMapper;

    public WorkoutSession getWorkoutSessionEntityById(Long workoutSessionId, User user) {
        return workoutSessionRepository.findByIdAndUser(workoutSessionId, user).orElseThrow(() -> new NotFoundException("Trénink nenalezen!"));
    }

    public List<WorkoutSessionDTO> getWorkoutSessionsByUser() {
        User user = userContext.getAuthenticatedUser();

        List<WorkoutSession> workoutSessions = workoutSessionRepository.findByUser(user);

        return workoutSessions
                .stream()
                .map(workoutSessionMapper::toWorkoutSessionDTO)
                .toList();
    }

    public WorkoutSessionDTO getWorkoutSessionById(Long id) {
        User user = userContext.getAuthenticatedUser();

        WorkoutSession workoutSession = getWorkoutSessionEntityById(id, user);

        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }

    @Transactional
    public WorkoutSessionDTO createWorkoutSession(WorkoutSessionRequest workoutSessionRequest) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanEntityById(workoutSessionRequest.trainingPlanId(), user);

        WorkoutSession workoutSession = WorkoutSession.builder()
                .date(workoutSessionRequest.date())
                .user(user)
                .trainingPlan(trainingPlan)
                .workoutSessionExercises(new ArrayList<>())
                .build();

        for (Exercise exercise: trainingPlan.getExercises()) {
            WorkoutSessionExercise workoutSessionExercise = WorkoutSessionExercise.builder()
                    .workoutSession(workoutSession)
                    .exercise(exercise)
                    .workoutSessionExerciseSets(new ArrayList<>())
                    .build();

            exercise.getWorkoutSessionExercises().add(workoutSessionExercise);
            workoutSession.getWorkoutSessionExercises().add(workoutSessionExercise);
        }

        workoutSession = workoutSessionRepository.save(workoutSession);

        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }

    @Transactional
    public void deleteWorkoutSessionById(Long id) {
        User user = userContext.getAuthenticatedUser();

        getWorkoutSessionEntityById(id, user);

        workoutSessionRepository.deleteById(id);
    }

    @Transactional
    public WorkoutSessionDTO createExerciseSet(
            Long workoutSessionId,
            Long workoutSessionExerciseId,
            WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest
    ) {
        User user = userContext.getAuthenticatedUser();

        WorkoutSession workoutSession = getWorkoutSessionEntityById(workoutSessionId, user);

        List<WorkoutSessionExercise> workoutSessionExercises = workoutSession.getWorkoutSessionExercises().stream().filter(wse -> wse.getId().equals(workoutSessionExerciseId)).toList();

        if (workoutSessionExercises.isEmpty()) throw new NotFoundException("Cvik v tréninku nenalezen!");

        WorkoutSessionExerciseSet workoutExerciseSet = WorkoutSessionExerciseSet.builder()
                .workoutSessionExercise(workoutSessionExercises.get(0))
                .repetitions(workoutSessionExerciseSetRequest.repetitions())
                .weight(workoutSessionExerciseSetRequest.weight())
                .build();

        workoutSessionExercises.get(0).getWorkoutSessionExerciseSets().add(workoutExerciseSet);
        workoutSession = workoutSessionRepository.save(workoutSession);


        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }

    @Transactional
    public void deleteExerciseSetById(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId) {
        User user = userContext.getAuthenticatedUser();

        WorkoutSession workoutSession = getWorkoutSessionEntityById(workoutSessionId, user);
        workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession);
        workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId);

        workoutSessionExerciseSetService.deleteWorkoutSessionExerciseSetById(workoutSessionExerciseSetId);
    }

    @Transactional
    public WorkoutSessionDTO editExerciseSet(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId, WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest) {
        User user = userContext.getAuthenticatedUser();

        WorkoutSession workoutSession = getWorkoutSessionEntityById(workoutSessionId, user);
        workoutSessionExerciseService.getWorkoutSessionExerciseEntityByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession);
        WorkoutSessionExerciseSet exerciseSet = workoutSessionExerciseSetService.getWorkoutSessionExerciseSetEntityById(workoutSessionExerciseSetId);

        exerciseSet.setRepetitions(workoutSessionExerciseSetRequest.repetitions());
        exerciseSet.setWeight(workoutSessionExerciseSetRequest.weight());

        workoutSessionExerciseSetService.saveWorkoutSessionExerciseSet(exerciseSet);

        return workoutSessionMapper.toWorkoutSessionDTO(workoutSession);
    }
}
