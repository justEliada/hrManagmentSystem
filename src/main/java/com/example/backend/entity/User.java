package com.example.backend.entity;

import com.example.backend.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class User extends BaseEntity{
    @NotBlank(message = "Username is required")
    @Length(max = 45, message = "Username should contain max 45 chars")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "First name is required")
    @Length(max = 45, message = "First name should contain max 45 chars")
    private String firstName;

    @Length(max = 45, message = "Last name should contain max 45 chars")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Length(max = 60, message = "Password should contain max 60 chars")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Role is required")
    private UserRole role;

    @Min(value = 0, message = "Min of days off is 0")
    @Max(value = 20, message = "Max of days off is 20")
    @Builder.Default
    private Integer daysOff = 20;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Timesheet> timeSheets;
}
