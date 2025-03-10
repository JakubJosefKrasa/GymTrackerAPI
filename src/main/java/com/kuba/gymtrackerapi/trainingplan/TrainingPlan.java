package com.kuba.gymtrackerapi.trainingplan;


import static jakarta.persistence.FetchType.LAZY;

import com.kuba.gymtrackerapi.exercise.Exercise;
import com.kuba.gymtrackerapi.user.User;
import com.kuba.gymtrackerapi.workoutsession.WorkoutSession;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class TrainingPlan {

    @Id
    @GeneratedValue
    private Long id;

    private String trainingPlanName;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(fetch = LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "training_plan_exercise",
            joinColumns = @JoinColumn(name = "training_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    @OrderColumn(name = "arrangement_index")
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "trainingPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<WorkoutSession> workoutSessions = new HashSet<>();
}
