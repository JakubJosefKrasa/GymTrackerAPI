package com.kuba.GymTrackerAPI.trainingPlan;

import com.kuba.GymTrackerAPI.exceptions.BadRequestException;
import com.kuba.GymTrackerAPI.exceptions.NotFoundException;
import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.exercise.ExerciseRepository;
import com.kuba.GymTrackerAPI.pagination.PaginationDTO;
import com.kuba.GymTrackerAPI.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final TrainingPlanRepository trainingPlanRepository;
    private final ExerciseRepository exerciseRepository;
    private final TrainingPlanDTOMapper trainingPlanDTOMapper;

    @Override
    public PaginationDTO<TrainingPlanDTO> getTrainingPlansByUser(int pageNumber, int pageSize, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        Pageable page;
        if (pageNumber >= 0) {
            page = PageRequest.of(pageNumber, pageSize);
        } else {
            page = Pageable.unpaged();
        }

        Page<TrainingPlan> trainingPlanPage = trainingPlanRepository.findByUser(page, user);

        List<TrainingPlanDTO> trainingPlans = trainingPlanPage
                .getContent()
                .stream()
                .map(trainingPlanDTOMapper)
                .toList();

        return new PaginationDTO<>(
                trainingPlans,
                trainingPlanPage.getTotalElements(),
                trainingPlanPage.hasPrevious(),
                trainingPlanPage.hasNext()
        );
    }

    @Override
    public TrainingPlanDTO getTrainingPlanById(Long id, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));

        return trainingPlanDTOMapper.apply(trainingPlan);
    }

    @Override
    public TrainingPlanDTO createTrainingPlan(TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlanToBeSaved = TrainingPlan.builder()
                .trainingPlanName(trainingPlanRequest.trainingPlanName())
                .user(user)
                .exercises(new HashSet<>())
                .build();

        TrainingPlan savedTrainingPlan = trainingPlanRepository.save(trainingPlanToBeSaved);

        return trainingPlanDTOMapper.apply(savedTrainingPlan);
    }

    @Override
    public void deleteTrainingPlanById(Long id, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
        trainingPlanRepository.delete(trainingPlan);
    }

    @Override
    public TrainingPlanDTO changeTrainingPlanName(Long id, TrainingPlanRequest trainingPlanRequest, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(id, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
        trainingPlan.setTrainingPlanName(trainingPlanRequest.trainingPlanName());

        TrainingPlan updatedTrainingPlan = trainingPlanRepository.save(trainingPlan);

        return trainingPlanDTOMapper.apply(updatedTrainingPlan);
    }

    @Override
    public TrainingPlanDTO addExerciseInTrainingPlan(Long trainingPlanId, Long exerciseId, Authentication authenticatedUser) {
        User user = (User) authenticatedUser.getPrincipal();

        TrainingPlan trainingPlan = trainingPlanRepository.findByIdAndUser(trainingPlanId, user).orElseThrow(() -> new NotFoundException("Tréninkový plán nenalezen!"));
        Exercise exercise = exerciseRepository.findByIdAndUser(exerciseId, user).orElseThrow(() -> new NotFoundException("Cvik nenalezen!"));

        if (trainingPlan.getExercises().contains(exercise)) throw new BadRequestException("Cvik může být pouze jednou v tréninkovém plánu!");

        trainingPlan.getExercises().add(exercise);

        TrainingPlan updatedTrainingPlan = trainingPlanRepository.save(trainingPlan);

        return trainingPlanDTOMapper.apply(updatedTrainingPlan);
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
