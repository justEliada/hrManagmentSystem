package com.example.backend.entity;

import com.example.backend.enums.TimeSheetStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Entity
@Table(name = "timeSheets")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Timesheet extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "timeSheets"})
    private User user;

    @NotNull(message = "Starting date is required!")
    private LocalDate fromDate;

    @NotNull(message = "The end date is required!")
    private LocalDate toDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private TimeSheetStatus status;

    private String notes;

}
