package com.kuba.gymtrackerapi.exercise;

import com.kuba.gymtrackerapi.exceptions.NotFoundException;
import com.kuba.gymtrackerapi.pagination.PaginationDTO;
import com.kuba.gymtrackerapi.security.UserContext;
import com.kuba.gymtrackerapi.trainingplan.TrainingPlan;
import com.kuba.gymtrackerapi.trainingplan.TrainingPlanService;
import com.kuba.gymtrackerapi.user.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ExerciseService {

    private final UserContext userContext;

    private final ExerciseRepository exerciseRepository;

    private final TrainingPlanService trainingPlanService;

    private final ExerciseMapper exerciseMapper;

    public ExerciseService(
            UserContext userContext,
            ExerciseRepository exerciseRepository,
            @Lazy TrainingPlanService trainingPlanService,
            ExerciseMapper exerciseMapper
    ) {
        this.userContext = userContext;
        this.exerciseRepository = exerciseRepository;
        this.trainingPlanService = trainingPlanService;
        this.exerciseMapper = exerciseMapper;
    }

    public Exercise getExerciseEntityById(Long id, User user) {
        log.info("[METHOD]: getExerciseEntityById - Fetching exercise by ID: {} and user_id: {}", id, user.getId());

        return exerciseRepository.findByIdAndUser(id, user).orElseThrow(() -> {
            log.warn("[METHOD]: getExerciseEntityById - exercise was not found by ID: {} and user_id: {}",
                     id,
                     user.getId()
            );

            return new NotFoundException("Cvik nenalezen!");
        });
    }

    public PaginationDTO<ExerciseDTO> getExercisesByUser(int pageNumber, int pageSize) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: getExercisesByUser - Fetching exercises for user_id: {} - pageNumber: {} - pageSize: {}",
                user.getId(),
                pageNumber,
                pageSize
        );

        Pageable page = pageNumber >= 0 ? PageRequest.of(pageNumber, pageSize) : Pageable.unpaged();

        Page<Exercise> exercisePage = exerciseRepository.findByUser(page, user);

        List<ExerciseDTO> exercisesByUser = exercisePage.getContent()
                                                        .stream()
                                                        .map(exerciseMapper::toExerciseDTO)
                                                        .toList();

        return new PaginationDTO<>(exercisesByUser,
                                   exercisePage.getTotalElements(),
                                   exercisePage.hasPrevious(),
                                   exercisePage.hasNext()
        );
    }

    public List<ExerciseDTO> getExercisesNotInTrainingPlan(Long trainingPlanId) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: getExercisesNotInTrainingPlan - Fetching exercises not in training plan by trainingPlanId: {} and user_id: {}",
                trainingPlanId,
                user.getId()
        );

        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanEntityById(trainingPlanId, user);

        List<Exercise> exercises = exerciseRepository.findByUserAndTrainingPlansNotContaining(user, trainingPlan);

        return exercises.stream().map(exerciseMapper::toExerciseDTO).toList();
    }

    @Transactional
    public ExerciseDTO createExercise(ExerciseRequestDTO exerciseRequest) {
        User user = userContext.getAuthenticatedUser();

        log.info("[METHOD]: createExercise - Creating exercise with exerciseRequest: {} for user_id: {}",
                 exerciseRequest,
                 user.getId()
        );

        Exercise exerciseToBeSaved = Exercise.builder().exerciseName(exerciseRequest.exerciseName()).user(user).build();

        exerciseToBeSaved = exerciseRepository.save(exerciseToBeSaved);

        log.info("[METHOD]: createExercise - Exercise created with exerciseName: {} and ID: {}",
                 exerciseToBeSaved.getExerciseName(),
                 exerciseToBeSaved.getId()
        );

        return exerciseMapper.toExerciseDTO(exerciseToBeSaved);
    }

    @Transactional
    public void deleteExerciseById(Long id) {
        User user = userContext.getAuthenticatedUser();

        log.info("[METHOD]: deleteExerciseById - Deleting exercise by ID: {} and user_id: {}", id, user.getId());

        Exercise exercise = getExerciseEntityById(id, user);

        exercise.getTrainingPlans().forEach(trainingPlan -> {
            log.info("[METHOD]: deleteExerciseById - Removing exercise by ID: {} from training plan by ID: {}",
                     id,
                     trainingPlan.getId()
            );

            trainingPlan.getExercises().remove(exercise);
        });

        exerciseRepository.delete(exercise);

        log.info("[METHOD]: deleteExerciseById - exercise by ID: {} was deleted", exercise.getId());
    }

    @Transactional
    public ExerciseDTO changeExerciseName(Long id, ExerciseRequestDTO exerciseRequest) {
        User user = userContext.getAuthenticatedUser();

        log.info(
                "[METHOD]: changeExerciseName - Changing exerciseName of exercise by ID: {} with exerciseRequest {} for user_id: {}",
                id,
                exerciseRequest,
                user.getId()
        );

        Exercise exercise = getExerciseEntityById(id, user);

        exercise.setExerciseName(exerciseRequest.exerciseName());

        exercise = exerciseRepository.save(exercise);

        log.info("[METHOD]: changeExerciseName - Changed exerciseName of exercise by ID: {} to {}",
                 id,
                 exercise.getExerciseName()
        );

        return exerciseMapper.toExerciseDTO(exercise);
    }
}
