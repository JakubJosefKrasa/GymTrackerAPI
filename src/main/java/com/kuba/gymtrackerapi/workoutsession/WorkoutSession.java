package com.kuba.gymtrackerapi.workoutsession;

import static jakarta.persistence.FetchType.LAZY;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuba.gymtrackerapi.trainingplan.TrainingPlan;
import com.kuba.gymtrackerapi.user.User;
import com.kuba.gymtrackerapi.workoutsessionexercise.WorkoutSessionExercise;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import java.time.LocalDate;
import java.util.LinkedHashSet;
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
public class WorkoutSession {

    @Id
    @GeneratedValue
    private Long id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "training_plan_id")
    private TrainingPlan trainingPlan;

    @OrderBy("id ASC")
    @OneToMany(mappedBy = "workoutSession", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkoutSessionExercise> workoutSessionExercises = new LinkedHashSet<>();
}
