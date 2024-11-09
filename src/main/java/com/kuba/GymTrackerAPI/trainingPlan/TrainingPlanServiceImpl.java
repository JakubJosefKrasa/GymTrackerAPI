package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exceptions.BadRequestException;
import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.exercise.ExerciseRepository;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final TrainingPlanExercisesDTOMapper trainingPlanExercisesDTOMapper;

    @Override
    public PaginationDTO<TrainingPlanExercisesDTO> getTrainingPlansByUser(int pageNumber, int pageSize, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        Pageable page;
        if (pageNumber >= 0) {
            page = PageRequest.of(pageNumber, pageSize);
        } else {
            page = Pageable.unpaged();
        }

        Page<TrainingPlan> trainingPlanPage = trainingPlanRepository.findByUser(page, user);

        List<TrainingPlanExercisesDTO> trainingPlans = trainingPlanPage
                .getContent()
                .stream()
                .map(trainingPlanExercisesDTOMapper)
                .toList();

        return new PaginationDTO<>(
                trainingPlans,
                trainingPlanPage.getTotalElements(),
                trainingPlanPage.hasPrevious(),
                trainingPlanPage.hasNext()
        );
    }

    @Override
    public TrainingPlanExercisesDTO getTrainingPlanById(Long id, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));

        return trainingPlanExercisesDTOMapper.apply(trainingPlan);
    }

    @Override
    public TrainingPlanExercisesDTO createTrainingPlan(TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlanToBeSaved = TrainingPlan.builder()
                .trainingPlanName(trainingPlanRequest.trainingPlanName())
                .user(user)
                .exercises(new HashSet<>())
                .build();

        trainingPlanToBeSaved = trainingPlanRepository.save(trainingPlanToBeSaved);

        return trainingPlanExercisesDTOMapper.apply(trainingPlanToBeSaved);
    }

    @Override
    public void deleteTrainingPlanById(Long id, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
        trainingPlanRepository.delete(trainingPlan);
    }

    @Override
    public TrainingPlanExercisesDTO changeTrainingPlanName(Long id, TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
        trainingPlan.setTrainingPlanName(trainingPlanRequest.trainingPlanName());
        trainingPlan = trainingPlanRepository.save(trainingPlan);

        return trainingPlanExercisesDTOMapper.apply(trainingPlan);
    }

    @Override
    public TrainingPlanExercisesDTO addExerciseInTrainingPlan(Long trainingPlanId, Long exerciseId, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(trainingPlanId, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
        Exercise exercise = exerciseRepository.findByIdAndUser(exerciseId, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));

        if (trainingPlan.getExercises().contains(exercise)) throw new BadRequestException("Cvik může být pouze jednou v tréninkovém plánu!");

        trainingPlan.getExercises().add(exercise);


        trainingPlan.getWorkoutSessions().forEach(workoutSession -> {
            WorkoutSessionExercise workoutSessionExercise = WorkoutSessionExercise.builder()
                            .workoutSession(workoutSession)
                            .exercise(exercise)
                            .workoutSessionExerciseSets(new ArrayList<>())
                            .build();

            workoutSession.getWorkoutSessionExercises().add(workoutSessionExercise);
        });

        trainingPlan = trainingPlanRepository.save(trainingPlan);

        return trainingPlanExercisesDTOMapper.apply(trainingPlan);
    }

    @Override
    public void removeExerciseFromTrainingPlan(Long trainingPlanId, Long exerciseId, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(trainingPlanId, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
        Exercise exercise = exerciseRepository.findByIdAndUser(exerciseId, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));

        trainingPlan.getExercises().remove(exercise);

        trainingPlanRepository.save(trainingPlan);
    }
}
