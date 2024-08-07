package com.kuba.GymTrackerAPI.exercise;

import com.kuba.GymTrackerAPI.trainingPlan.TrainingPlan;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Exercise {
    @Id
    @GeneratedValue
    private Long id;
    private String exerciseName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(mappedBy = "exercises", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<TrainingPlan> trainingPlans = new HashSet<>();
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSessionExercise> workoutSessionExercises = new ArrayList<>();
}
