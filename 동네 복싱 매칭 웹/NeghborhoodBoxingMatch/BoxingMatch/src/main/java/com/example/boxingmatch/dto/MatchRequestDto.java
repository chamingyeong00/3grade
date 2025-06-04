package com.example.boxingmatch.dto;

import com.example.boxingmatch.entity.BoxingMatch;
import com.example.boxingmatch.entity.User;
import com.example.boxingmatch.enums.MatchRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MatchRequestDto {
    private MatchRequestStatus status;
    private Long matchId;
    private Long userId;
    private User user;
    private BoxingMatch match;
}
