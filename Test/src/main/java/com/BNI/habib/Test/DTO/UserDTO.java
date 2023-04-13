package com.BNI.habib.Test.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;

    @NotNull
    @Pattern(regexp = "\\d{16}")
    private String ssn;

    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z ]*$")
    private String firstName;

    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z ]*$")
    private String middleName;

    @NotNull
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z ]*$")
    private String familyName;

    @NotNull
    @Past
    private LocalDate birthDate;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private String createdBy = "SYSTEM";

    private String updatedBy = "SYSTEM";

    private Boolean isActive = true;
}
