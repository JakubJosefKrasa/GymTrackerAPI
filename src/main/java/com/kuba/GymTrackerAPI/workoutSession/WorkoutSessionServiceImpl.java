package com.kuba.GymTrackerAPI.workoutSession;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlan;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlanRepository;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExerciseRepository;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSet;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetRepository;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSetRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutSessionServiceImpl implements WorkoutSessionService {
    private final WorkoutSessionRepository workoutSessionRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final WorkoutSessionExerciseRepository workoutSessionExerciseRepository;
    private final WorkoutSessionExerciseSetRepository workoutSessionExerciseSetRepository;

    private final WorkoutSessionDTOMapper workoutSessionDTOMapper;

    @Override
    public List<WorkoutSessionDTO> getWorkoutSessionsByUser(Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        List<WorkoutSession> workoutSessions = workoutSessionRepository.findByUser(user);

        return workoutSessions
                .stream()
                .map(workoutSessionDTOMapper)
                .toList();
    }

    @Override
    public WorkoutSessionDTO getWorkoutSessionById(Long id, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        WorkoutSession workoutSession = workoutSessionRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Trénink nenalezen!"));

        return workoutSessionDTOMapper.apply(workoutSession);
    }

    @Override
    @Transactional
    public WorkoutSessionDTO createWorkoutSession(WorkoutSessionRequest workoutSessionRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(workoutSessionRequest.trainingPlanId(), user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));

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

        return workoutSessionDTOMapper.apply(workoutSession);
    }

    @Override
    public void deleteWorkoutSessionById(Long id, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        workoutSessionRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Trénink nenalezen!"));

        workoutSessionRepository.deleteById(id);
    }

    @Override
    public WorkoutSessionDTO createExerciseSet(
            Long workoutSessionId,
            Long workoutSessionExerciseId,
            WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest,
            Authentication authenticatedUser
    ) {
        User user = (User) authenticatedUser.getPrincipal();

        WorkoutSession workoutSession = workoutSessionRepository.findByIdAndUser(workoutSessionId, user).orElseThrow(() -> new NotFoundException("Trénink nenalezen!"));

        List<WorkoutSessionExercise> workoutSessionExercises = workoutSession.getWorkoutSessionExercises().stream().filter(wse -> wse.getId().equals(workoutSessionExerciseId)).toList();
        if (workoutSessionExercises.isEmpty()) throw new NotFoundException("Cvik v tréninku nenalezen!");

        WorkoutSessionExerciseSet workoutExerciseSet = WorkoutSessionExerciseSet.builder()
                .workoutSessionExercise(workoutSessionExercises.get(0))
                .repetitions(workoutSessionExerciseSetRequest.repetitions())
                .weight(workoutSessionExerciseSetRequest.weight())
                .build();

        workoutSessionExercises.get(0).getWorkoutSessionExerciseSets().add(workoutExerciseSet);
        workoutSessionRepository.save(workoutSession);


        return workoutSessionDTOMapper.apply(workoutSession);
    }

    @Override
    public void deleteExerciseSetById(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        WorkoutSession workoutSession = workoutSessionRepository.findByIdAndUser(workoutSessionId, user).orElseThrow(() -> new NotFoundException("Trénink nenalezen!"));
        workoutSessionExerciseRepository.findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession).orElseThrow(() -> new NotFoundException("Cvik v tréninku nenalezen!"));
        workoutSessionExerciseSetRepository.findById(workoutSessionExerciseSetId).orElseThrow(() -> new NotFoundException("Série nenalezena"));

        workoutSessionExerciseSetRepository.deleteById(workoutSessionExerciseSetId);
    }

    @Override
    public WorkoutSessionDTO editExerciseSet(Long workoutSessionId, Long workoutSessionExerciseId, Long workoutSessionExerciseSetId, WorkoutSessionExerciseSetRequest workoutSessionExerciseSetRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        WorkoutSession workoutSession = workoutSessionRepository.findByIdAndUser(workoutSessionId, user).orElseThrow(() -> new NotFoundException("Trénink nenalezen!"));
        workoutSessionExerciseRepository.findByIdAndWorkoutSession(workoutSessionExerciseId, workoutSession).orElseThrow(() -> new NotFoundException("Cvik v tréninku nenalezen!"));
        WorkoutSessionExerciseSet exerciseSet = workoutSessionExerciseSetRepository.findById(workoutSessionExerciseSetId).orElseThrow(() -> new NotFoundException("Série nenalezena"));

        exerciseSet.setRepetitions(workoutSessionExerciseSetRequest.repetitions());
        exerciseSet.setWeight(workoutSessionExerciseSetRequest.weight());

        workoutSessionExerciseSetRepository.save(exerciseSet);

        return workoutSessionDTOMapper.apply(workoutSession);
    }
}
