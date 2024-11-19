package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exceptions.BadRequestException;
import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.exercise.ExerciseService;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.security.UserContext;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final UserContext userContext;
    private final TrainingPlanRepository trainingPlanRepository;
    private final ExerciseService exerciseService;
    private final TrainingPlanMapper trainingPlanMapper;

    public TrainingPlan getTrainingPlanEntityById(Long id, User user) {
        return trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
    }

    @Override
    public PaginationDTO<TrainingPlanExercisesDTO> getTrainingPlansByUser(int pageNumber, int pageSize) {
        User user = userContext.getAuthenticatedUser();

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
                .map(trainingPlanMapper::toTrainingPlanExercisesDTO)
                .toList();

        return new PaginationDTO<>(
                trainingPlans,
                trainingPlanPage.getTotalElements(),
                trainingPlanPage.hasPrevious(),
                trainingPlanPage.hasNext()
        );
    }

    @Override
    public TrainingPlanExercisesDTO getTrainingPlanById(Long id) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlan = getTrainingPlanEntityById(id, user);

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlan);
    }

    @Override
    @Transactional
    public TrainingPlanExercisesDTO createTrainingPlan(TrainingPlanRequest trainingPlanRequest) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlanToBeSaved = TrainingPlan.builder()
                .trainingPlanName(trainingPlanRequest.trainingPlanName())
                .user(user)
                .exercises(new HashSet<>())
                .build();

        trainingPlanToBeSaved = trainingPlanRepository.save(trainingPlanToBeSaved);

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlanToBeSaved);
    }

    @Override
    @Transactional
    public void deleteTrainingPlanById(Long id) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlan = getTrainingPlanEntityById(id, user);
        trainingPlanRepository.delete(trainingPlan);
    }

    @Override
    @Transactional
    public TrainingPlanExercisesDTO changeTrainingPlanName(Long id, TrainingPlanRequest trainingPlanRequest) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlan = getTrainingPlanEntityById(id, user);
        trainingPlan.setTrainingPlanName(trainingPlanRequest.trainingPlanName());
        trainingPlan = trainingPlanRepository.save(trainingPlan);

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlan);
    }

    @Override
    @Transactional
    public TrainingPlanExercisesDTO addExerciseInTrainingPlan(Long trainingPlanId, Long exerciseId) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlan = getTrainingPlanEntityById(trainingPlanId, user);
        Exercise exercise = exerciseService.getExerciseEntityById(exerciseId, user);

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

        return trainingPlanMapper.toTrainingPlanExercisesDTO(trainingPlan);
    }

    @Override
    @Transactional
    public void removeExerciseFromTrainingPlan(Long trainingPlanId, Long exerciseId) {
        User user = userContext.getAuthenticatedUser();

        TrainingPlan trainingPlan = getTrainingPlanEntityById(trainingPlanId, user);
        Exercise exercise = exerciseService.getExerciseEntityById(exerciseId, user);

        trainingPlan.getExercises().remove(exercise);

        trainingPlanRepository.save(trainingPlan);
    }
}
