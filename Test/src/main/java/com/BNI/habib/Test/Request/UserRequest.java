package com.BNI.habib.Test.Request;

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
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class UserRequest {
    @NotNull
    @Size(min = 16, max=16)
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

    @Override
    public String toString() {
        return "UserRequest{" +
                "ssn='" + ssn + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", familyName='" + familyName + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
