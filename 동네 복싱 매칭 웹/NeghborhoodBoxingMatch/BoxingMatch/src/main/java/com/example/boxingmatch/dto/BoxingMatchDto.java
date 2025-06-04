package com.example.boxingmatch.dto;

import com.example.boxingmatch.enums.GenderCondition;
import com.example.boxingmatch.enums.MatchStatus;
import com.example.boxingmatch.enums.Region;
import com.example.boxingmatch.enums.SkillLevelCondition;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoxingMatchDto {
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // ⬅️ 이거 꼭 추가!
    private LocalDateTime scheduledTime;
    private Region region;
    private GenderCondition genderCondition;
    private SkillLevelCondition skilllevelCondition;
    private String description;
    private String location;
    private MatchStatus matchstatus = MatchStatus.OPEN;

}