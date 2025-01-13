package com.kuba.gymtrackerapi.workoutsessionexercise;

import static jakarta.persistence.FetchType.LAZY;

import com.kuba.gymtrackerapi.exercise.Exercise;
import com.kuba.gymtrackerapi.workoutsession.WorkoutSession;
import com.kuba.gymtrackerapi.workoutsessionexerciseset.WorkoutSessionExerciseSet;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.util.HashSet;
import java.util.Set;
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
public class WorkoutSessionExercise {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "workoutSessionExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkoutSessionExerciseSet> workoutSessionExerciseSets = new HashSet<>();
}
