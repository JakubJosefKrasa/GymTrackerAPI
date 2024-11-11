package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.security.UserContext;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlan;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlanRepository;
import com.kuba.GymTrackerAPI.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final UserContext userContext;
    private final ExerciseRepository exerciseRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final ExerciseDTOMapper exerciseDTOMapper;

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
                .map(exerciseDTOMapper)
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

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(trainingPlanId, user)
                .orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));

        Page<Exercise> exercises = exerciseRepository.findByUser(Pageable.unpaged(), user);

        return exercises.stream()
                .filter(
                        exercise -> !trainingPlan.getExercises().contains(exercise)
                )
                .map(exerciseDTOMapper)
                .toList();
    }

    @Override
    @Transactional
    public ExerciseDTO createExercise(ExerciseRequest exerciseRequest) {
        User user = userContext.getAuthenticatedUser();

        Exercise exerciseToBeSaved = Exercise.builder().exerciseName(exerciseRequest.exerciseName()).user(user).build();
        exerciseToBeSaved = exerciseRepository.save(exerciseToBeSaved);

        return exerciseDTOMapper.apply(exerciseToBeSaved);
    }

    @Override
    @Transactional
    public void deleteExerciseById(Long id) {
        User user = userContext.getAuthenticatedUser();

        Exercise exercise = exerciseRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));
        exercise.getTrainingPlans().forEach(trainingPlan -> trainingPlan.getExercises().remove(exercise));

        exerciseRepository.delete(exercise);
    }

    @Override
    @Transactional
    public ExerciseDTO changeExerciseName(Long id, ExerciseRequest exerciseRequest) {
        User user = userContext.getAuthenticatedUser();

        Exercise exercise = exerciseRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));
        exercise.setExerciseName(exerciseRequest.exerciseName());
        exercise = exerciseRepository.save(exercise);

        return exerciseDTOMapper.apply(exercise);
    }
}
