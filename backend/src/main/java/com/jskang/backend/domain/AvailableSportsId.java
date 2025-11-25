package com.jskang.backend.domain;

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

    private Long userId;
    private Long sportId;


}
