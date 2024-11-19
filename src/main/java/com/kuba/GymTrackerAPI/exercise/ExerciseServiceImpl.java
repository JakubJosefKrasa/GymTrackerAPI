package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.security.UserContext;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlan;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlanService;
import com.kuba.GymTrackerAPI.user.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private final UserContext userContext;
    private final ExerciseRepository exerciseRepository;
    private final TrainingPlanService trainingPlanService;
    private final ExerciseMapper exerciseMapper;

    public ExerciseServiceImpl(UserContext userContext, ExerciseRepository exerciseRepository, @Lazy TrainingPlanService trainingPlanService, ExerciseMapper exerciseMapper) {
        this.userContext = userContext;
        this.exerciseRepository = exerciseRepository;
        this.trainingPlanService = trainingPlanService;
        this.exerciseMapper = exerciseMapper;
    }

    public Exercise getExerciseEntityById(Long id, User user) {
        return exerciseRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));
    }

    @Override
    public PaginationDTO<ExerciseDTO> getExercisesByUser(int pageNumber, int pageSize) {
        User user = userContext.getAuthenticatedUser();

        Pageable page;
        if (pageNumber >= 0) {
            page = PageRequest.of(pageNumber, pageSize);
        } else {
            page = Pageable.unpaged();
        }

        Page<Exercise> exercisePage = exerciseRepository.findByUser(page, user);

        List<ExerciseDTO> exercisesByUser = exercisePage
                .getContent()
                .stream()
                .map(exerciseMapper::toExerciseDTO)
                .toList();

        return new PaginationDTO<>(
                exercisesByUser,
                exercisePage.getTotalElements(),
                exercisePage.hasPrevious(),
                exercisePage.hasNext()
        );
    }

    @Override
    public List<ExerciseDTO> getExercisesNotInTrainingPlan(Long trainingPlanId) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlan = trainingPlanService.getTrainingPlanEntityById(trainingPlanId, user);

        List<Exercise> exercises = exerciseRepository.findByUserAndTrainingPlansNotContaining(user, trainingPlan);

        return exercises.stream().map(exerciseMapper::toExerciseDTO).toList();
    }

    @Override
    @Transactional
    public ExerciseDTO createExercise(ExerciseRequest exerciseRequest) {
        User user = userContext.getAuthenticatedUser();

        Exercise exerciseToBeSaved = Exercise.builder().exerciseName(exerciseRequest.exerciseName()).user(user).build();
        exerciseToBeSaved = exerciseRepository.save(exerciseToBeSaved);

        return exerciseMapper.toExerciseDTO(exerciseToBeSaved);
    }

    @Override
    @Transactional
    public void deleteExerciseById(Long id) {
        User user = userContext.getAuthenticatedUser();

        Exercise exercise = getExerciseEntityById(id, user);
        exercise.getTrainingPlans().forEach(trainingPlan -> trainingPlan.getExercises().remove(exercise));

        exerciseRepository.delete(exercise);
    }

    @Override
    @Transactional
    public ExerciseDTO changeExerciseName(Long id, ExerciseRequest exerciseRequest) {
        User user = userContext.getAuthenticatedUser();

        Exercise exercise = getExerciseEntityById(id, user);
        exercise.setExerciseName(exerciseRequest.exerciseName());
        exercise = exerciseRepository.save(exercise);

        return exerciseMapper.toExerciseDTO(exercise);
    }
}
