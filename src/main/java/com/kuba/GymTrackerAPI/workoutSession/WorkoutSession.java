package com.kuba.GymTrackerAPI.workoutSession;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlan;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutSession {
    @Id
    @GeneratedValue
    private Long id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "training_plan_id")
    private TrainingPlan trainingPlan;
    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSessionExercise> workoutSessionExercises = new ArrayList<>();
}
