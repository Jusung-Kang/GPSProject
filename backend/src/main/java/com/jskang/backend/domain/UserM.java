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
@Table(name = "USER_M")

public class UserM {

    @Id
    @Column(name = "Id", length = 100, nullable = false)
    private String id;

    @Column(name = "Email", length = 100, nullable = false)
    private String email;

    @Column(name = "PhoneNumber", length = 11, nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "userM", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<AvailableSports> availableSports = new HashSet<>();

    @OneToMany(mappedBy = "userM")
    @Builder.Default
    @ToString.Exclude
    private Set<ExerciseHistory> exerciseHistory = new HashSet<>();


}
