package com.jskang.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
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
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private UserM userM;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sportId")
    @JoinColumn(name = "sport_id")
    @ToString.Exclude
    private SportType sportType;

    public void setUserM(UserM userM) {
        this.userM = userM;
    }

    public void changeAvailableInfo(String level, String positionCd) {
        this.level = level;
        this.positionCd = positionCd;
    }


}
