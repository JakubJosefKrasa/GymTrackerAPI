package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.user.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseDTOMapper exerciseDTOMapper;

    @Override
    public PaginationDTO<ExerciseDTO> getExercisesByUser(int pageNumber, int pageSize, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

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
    public ExerciseDTO createExercise(ExerciseRequest exerciseRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        Exercise exerciseToBeSaved = Exercise.builder().exerciseName(exerciseRequest.exerciseName()).user(user).build();
        Exercise savedExercise = exerciseRepository.save(exerciseToBeSaved);

        return exerciseDTOMapper.apply(savedExercise);
    }

    @Override
    @Transactional
    public void deleteExerciseById(Long id, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        Exercise exercise = exerciseRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));
        exercise.getTrainingPlans().forEach(trainingPlan -> trainingPlan.getExercises().remove(exercise));

        exerciseRepository.delete(exercise);
    }

    @Override
    public ExerciseDTO changeExerciseName(Long id, ExerciseRequest exerciseRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        Exercise exercise = exerciseRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));
        exercise.setExerciseName(exerciseRequest.exerciseName());

        Exercise updatedExercise = exerciseRepository.save(exercise);

        return exerciseDTOMapper.apply(updatedExercise);
    }
}
