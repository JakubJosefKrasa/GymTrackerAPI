package com.kuba.GymTrackerAPI.workoutSessionExercise;

import com.kuba.GymTrackerAPI.exercise.Exercise;
import com.kuba.GymTrackerAPI.workoutSession.WorkoutSession;
import com.kuba.GymTrackerAPI.workoutSessionExerciseSet.WorkoutSessionExerciseSet;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutSessionExercise {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;
    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
    @OneToMany(mappedBy = "workoutSessionExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSessionExerciseSet> workoutSessionExerciseSets = new ArrayList<>();
}
