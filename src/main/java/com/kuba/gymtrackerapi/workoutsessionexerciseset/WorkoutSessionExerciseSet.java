package com.kuba.gymtrackerapi.workoutsessionexerciseset;

import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExercise;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
