package com.teachersession.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileDto {
    private Long id;
    private Long userId;
    private String bio;
    private String expertiseArea;
    private BigDecimal ratingAverage;
}
