package com.example.boxingmatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Data
public class ReviewDto {
    private int rating;
    private String comment;
    private Long matchId;
    private Long userId;
}