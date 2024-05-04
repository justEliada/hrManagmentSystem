package com.example.backend.dto.timeSheetDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import com.example.backend.entity.User;
import com.example.backend.enums.TimeSheetStatus;

@Data
@Builder
@AllArgsConstructor
public class TimeSheetResponseDto {
    private Long userId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private TimeSheetStatus status;
    private String notes;
    private String createdBy;
    private User user;
}
