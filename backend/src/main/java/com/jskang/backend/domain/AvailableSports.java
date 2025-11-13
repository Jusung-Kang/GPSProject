package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AVAILABLE_SPORTS")

public class AvailableSports {

    @EmbeddedId
    // AvailableSportsId에 id, sportId 존재
    private AvailableSportsId pk;

    @Column(name = "PositionCd", length = 4)
    private String positionCd;

    @Column(name = "Level", length = 8)
    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "Id")
    @ToString.Exclude
    private UserM userM;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sportId")
    @JoinColumn(name = "SportId")
    @ToString.Exclude
    private SportType sportType;


}
