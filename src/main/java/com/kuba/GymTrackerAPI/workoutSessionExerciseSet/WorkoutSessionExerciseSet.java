package com.kuba.GymTrackerAPI.workoutSessionExerciseSet;

import com.kuba.GymTrackerAPI.workoutSessionExercise.WorkoutSessionExercise;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WorkoutSessionExerciseSet {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "workout_session_exercise_id")
    private WorkoutSessionExercise workoutSessionExercise;
    private int repetitions;
    private float weight;
}
