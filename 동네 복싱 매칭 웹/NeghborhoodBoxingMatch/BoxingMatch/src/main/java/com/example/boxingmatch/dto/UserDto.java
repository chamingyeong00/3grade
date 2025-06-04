package com.example.boxingmatch.dto;

import com.example.boxingmatch.enums.Gender;
import com.example.boxingmatch.enums.Region;
import com.example.boxingmatch.enums.SkillLevelCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private String username;
    private String email;
    private String password;
    private Gender gender;
    private Region region;
    private SkillLevelCondition skilllevelCondition;

}
