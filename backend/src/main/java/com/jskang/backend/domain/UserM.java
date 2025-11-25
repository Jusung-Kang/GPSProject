package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_m")

public class UserM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;

    private String phoneNumber;

    @OneToMany(mappedBy = "userM", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<AvailableSports> availableSports = new HashSet<>();

    @OneToMany(mappedBy = "userM")
    @Builder.Default
    @ToString.Exclude
    private Set<ExerciseHistory> exerciseHistory = new HashSet<>();

    public void updateContactInfo(String email, String phoneNumber){
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void addAvailableSports(AvailableSports availableSports) {
        this.availableSports.add(availableSports);
        availableSports.setUserM(this);
    }

    public void addExerciseHistory(ExerciseHistory exerciseHistory) {
        this.exerciseHistory.add(exerciseHistory);
        exerciseHistory.setUserM(this);
    }

}
