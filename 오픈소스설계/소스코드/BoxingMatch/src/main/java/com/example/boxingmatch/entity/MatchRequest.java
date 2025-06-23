package com.example.boxingmatch.entity;

import com.example.boxingmatch.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class MatchRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="request_id")
    private Long requestId;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchRequestStatus status;

    // ✅ 매핑 필드 추가
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private BoxingMatch match;

    @Builder
    public MatchRequest(MatchRequestStatus status, User user, BoxingMatch match) {
        this.status = status;
        this.user = user;
        this.match = match;
    }

    public MatchRequest(User user, BoxingMatch match) {
        this.user = user;
        this.match = match;
        this.status = MatchRequestStatus.PENDING; // 기본 상태 설정 (옵션)
    }
}
