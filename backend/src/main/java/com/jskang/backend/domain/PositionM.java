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
@Table(name = "position_m")
public class PositionM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long positionId;
    private String positionNm;

    @OneToMany(mappedBy = "availableSports")
    @Builder.Default
    @ToString.Exclude
    private Set<AvailableSports> availableSports = new HashSet<>();

    public void changePositionNm(String positionNm){
        this.positionNm = positionNm;
    }
}
