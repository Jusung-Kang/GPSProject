package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_m")

public class UserM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Column(name = "Email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "PhoneNumber", length = 11, nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "userM", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<AvailableSports> availableSports = new HashSet<>();

    @OneToMany(mappedBy = "userM")
    @Builder.Default
    @ToString.Exclude
    private Set<ExerciseHistory> exerciseHistory = new HashSet<>();

    public void addAvailableSports(AvailableSports availableSports) {
        this.availableSports.add(availableSports);
        availableSports.setUserM(this);
    }

    public void addExerciseHistory(ExerciseHistory exerciseHistory) {
        this.exerciseHistory.add(exerciseHistory);
        exerciseHistory.setUserM(this);
    }

}
