package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "available_sports")
public class AvailableSports {

    @EmbeddedId
    // AvailableSportsId에 id, sportId 존재
    private AvailableSportsId pk;

    private String positionCd;

    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("id")
    @JoinColumn(name = "id")
    @ToString.Exclude
    private UserM userM;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sportId")
    @JoinColumn(name = "sport_id")
    @ToString.Exclude
    private SportType sportType;

}
