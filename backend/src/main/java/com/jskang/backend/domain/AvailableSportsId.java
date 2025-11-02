package com.jskang.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // 복합 키 클래스는 equals와 hashCode 구현이 필수
@Embeddable


public class AvailableSportsId implements Serializable {

    @Column(name = "Id", length = 100, nullable = false)
    private String id;

    @Column(name = "SportId", length = 8, nullable = false)
    private String sportId;


}
