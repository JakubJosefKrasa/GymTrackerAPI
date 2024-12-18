package com.kuba.gymtrackerapi.trainingplan;

import com.kuba.gymtrackerapi.exceptions.BadRequestException;
import com.kuba.gymtrackerapi.exceptions.NotFoundException;
import com.kuba.gymtrackerapi.exercise.Exercise;
import com.kuba.gymtrackerapi.exercise.ExerciseService;
import com.kuba.gymtrackerapi.pagination.PaginationDTO;
import com.kuba.gymtrackerapi.security.UserContext;
import com.kuba.gymtrackerapi.user.User;
import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExercise;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingPlanService {

    private final UserContext userContext;

    private final TrainingPlanRepository trainingPlanRepository;

    private final ExerciseService exerciseService;

    private final TrainingPlanMapper trainingPlanMapper;

    public TrainingPlan getTrainingPlanEntityById(Long id, User user) {
        log.info(
                "[METHOD]: getTrainingPlanEntityById - Fetching trainingPlan by ID: {} and user_id: {}",
                id,
                user.getId()
        );

        return trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> {
            log.warn(
                    "[METHOD]: getTrainingPlanEntityById - trainingPlan was not found by ID: {} and user_id: {}",
                    id,
                    user.getId()
            );

            return new NotFoundException("Tréninkový plán nenalezen!");
        });
    }

    public PaginationDTO<TrainingPlanExercisesDTO> getTrainingPlansByUser(int pageNumber, int pageSize) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: getTrainingPlansByUser - Fetching training plans for user_id: {} - pageNumber: {} - pageSize: {}",
                user.getId(),
                pageNumber,
                pageSize
        );

        Pageable page = pageNumber >= 0 ? PageRequest.of(pageNumber, pageSize) : Pageable.unpaged();

        Page<TrainingPlan> trainingPlanPage = trainingPlanRepository.findByUser(page, user);

        List<TrainingPlanExercisesDTO> trainingPlans = trainingPlanPage.getContent()
                                                                       .stream()
                                                                       .map(trainingPlanMapper::toTrainingPlanExercisesDTO)
                                                                       .toList();

        return new PaginationDTO<>(trainingPlans,
                                   trainingPlanPage.getTotalElements(),
                                   trainingPlanPage.hasPrevious(),
                                   trainingPlanPage.hasNext()
        );
    }

    public TrainingPlanExercisesDTO getTrainingPlanById(Long id) {
        User user = userContext.getAuthenticatedUser();

        log.info("[METHOD]: getTrainingPlanById - Fetching trainingPlan by ID: {} for user_id: {}", id, user.getId());

        TrainingPlan trainingPlan = getTrainingPlanEntityById(id, user);

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlan);
    }

    @Transactional
    public TrainingPlanExercisesDTO createTrainingPlan(TrainingPlanRequestDTO trainingPlanRequest) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: createTrainingPlan - Creating trainingPlan with trainingPlanRequest: {} for user_id: {}",
                trainingPlanRequest,
                user.getId()
        );

        TrainingPlan trainingPlanToBeSaved = TrainingPlan.builder()
                                                         .trainingPlanName(trainingPlanRequest.trainingPlanName())
                                                         .user(user)
                                                         .exercises(new HashSet<>())
                                                         .build();

        trainingPlanToBeSaved = trainingPlanRepository.save(trainingPlanToBeSaved);
        log.info(
                "[METHOD]: createTrainingPlan - trainingPlan created with trainingPlanName: {} and ID: {}",
                trainingPlanToBeSaved.getTrainingPlanName(),
                trainingPlanToBeSaved.getId()
        );

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlanToBeSaved);
    }

    @Transactional
    public void deleteTrainingPlanById(Long id) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: deleteTrainingPlanById - Deleting trainingPlan by ID: {} and user_id: {}",
                id,
                user.getId()
        );

        TrainingPlan trainingPlan = getTrainingPlanEntityById(id, user);

        trainingPlanRepository.delete(trainingPlan);

        log.info("[METHOD]: deleteTrainingPlanById - trainingPlan with ID: {} was deleted", id);
    }

    @Transactional
    public TrainingPlanExercisesDTO changeTrainingPlanName(Long id, TrainingPlanRequestDTO trainingPlanRequest) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: changeTrainingPlanName - Changing trainingPlanName of trainingPlan by ID: {} with trainingPlanRequest {} for user_id: {}",
                id,
                trainingPlanRequest,
                user.getId()
        );

        TrainingPlan trainingPlan = getTrainingPlanEntityById(id, user);

        trainingPlan.setTrainingPlanName(trainingPlanRequest.trainingPlanName());

        trainingPlan = trainingPlanRepository.save(trainingPlan);

        log.info(
                "[METHOD]: changeTrainingPlanName - Changed trainingPlanName of trainingPlan by ID: {} to {}",
                id,
                trainingPlan.getTrainingPlanName()
        );

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlan);
    }

    @Transactional
    public TrainingPlanExercisesDTO addExerciseInTrainingPlan(Long trainingPlanId, Long exerciseId) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: addExerciseInTrainingPlan - Adding exercise by ID: {} in trainingPlan by ID: {} for user_id: {}",
                exerciseId,
                trainingPlanId,
                user.getId()
        );

        TrainingPlan trainingPlan = getTrainingPlanEntityById(trainingPlanId, user);
        Exercise exercise = exerciseService.getExerciseEntityById(exerciseId, user);

        if (trainingPlan.getExercises().contains(exercise)) {
            log.error(
                    "[METHOD]: addExerciseInTrainingPlan - Exercise by ID: {} is already in trainingPlan by ID: {} for user_id: {}",
                    exerciseId,
                    trainingPlanId,
                    user.getId()
            );

            throw new BadRequestException("Cvik může být pouze jednou v tréninkovém plánu!");
        }

        trainingPlan.getExercises().add(exercise);

        trainingPlan.getWorkoutSessions().forEach(workoutSession -> {
            WorkoutSessionExercise workoutSessionExercise = WorkoutSessionExercise.builder()
                                                                                  .workoutSession(workoutSession)
                                                                                  .exercise(exercise)
                                                                                  .workoutSessionExerciseSets(new ArrayList<>())
                                                                                  .build();

            log.info(
                    "[METHOD]: addExerciseInTrainingPlan - Creating workoutSessionExercise for exercise by ID: {} for workoutSession by ID {} in trainingPlan by ID: {} for user_id: {}",
                    exerciseId, workoutSession.getId(), trainingPlanId, user.getId()
            );
            workoutSession.getWorkoutSessionExercises().add(workoutSessionExercise);
        });

        log.info("[METHOD]: addExerciseInTrainingPlan - Saving added exercise by ID: {} in trainingPlan by ID: {} for user_id: {}", exerciseId, trainingPlanId, user.getId());
        trainingPlan = trainingPlanRepository.save(trainingPlan);

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlan);
    }

    @Transactional
    public void removeExerciseFromTrainingPlan(Long trainingPlanId, Long exerciseId) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: removeExerciseFromTrainingPlan - Removing exercise by ID: {} from trainingPlan by ID: {} for user_id: {}",
                exerciseId,
                trainingPlanId,
                user.getId()
        );

        TrainingPlan trainingPlan = getTrainingPlanEntityById(trainingPlanId, user);
        Exercise exercise = exerciseService.getExerciseEntityById(exerciseId, user);

        trainingPlan.getExercises().remove(exercise);

        trainingPlanRepository.save(trainingPlan);

        log.info(
                "[METHOD]: removeExerciseFromTrainingPlan - Exercise by ID: {} was removed from trainingPlan by ID: {} for user_id: {}",
                exerciseId,
                trainingPlanId,
                user.getId()
        );
    }
}
