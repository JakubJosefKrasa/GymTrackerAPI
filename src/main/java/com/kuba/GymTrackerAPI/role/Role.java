package com.kuba.GymTrackerAPI.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kuba.GymTrackerAPI.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class Role {
    @Id@
    GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> user;
}
