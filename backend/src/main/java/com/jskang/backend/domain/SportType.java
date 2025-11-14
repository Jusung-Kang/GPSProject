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
@Table(name = "sport_type")

public class SportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SportId", nullable = false)
    private Long sportId;

    @Column(name = "SportNm", length = 200, nullable = false)
    private String sportNm;

    @OneToMany(mappedBy = "sportType", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<AvailableSports> availableSports = new HashSet<>();

    @OneToMany(mappedBy = "sportType")
    @Builder.Default
    @ToString.Exclude
    private Set<ExerciseHistory> exerciseHistory = new HashSet<>();

}
