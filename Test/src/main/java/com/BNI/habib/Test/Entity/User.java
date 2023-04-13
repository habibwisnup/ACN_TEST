package com.BNI.habib.Test.Entity;

import com.BNI.habib.Test.Entity.Fields.UserFields;
import com.BNI.habib.Test.Entity.Fields.UserSettingFields;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = UserFields.ID)
    private Long id;

    @Column(name = UserFields.SSN, nullable = false, unique = true, length = 16)
    @Pattern(regexp = "\\d{16}")
    private String ssn;

    @Column(name = UserFields.FIRST_NAME, nullable = false, length = 100)
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z]*$")
    private String firstName;

    @Column(name = UserFields.MIDDLE_NAME, length = 100)
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z]*$")
    private String middleName;

    @Column(name = UserFields.FAMILY_NAME, length = 100)
    @Size(min = 3, max = 100)
    @Pattern(regexp = "^[a-zA-Z]*$")
    private String familyName;

    @Column(name = UserFields.BIRTH_DATE, nullable = false)
    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Column(name = UserFields.CREATED_TIME, nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdTime;

    @Column(name = UserFields.UPDATED_TIME, nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedTime;

    @Column(name = UserFields.CREATED_BY, nullable = false, length = 100)
    @Size(max = 100)
    private String createdBy = "SYSTEM";

    @Column(name = UserFields.UPDATED_BY, nullable = false, length = 100)
    @Size(max = 100)
    private String updatedBy = "SYSTEM";

    @Column(name = UserFields.IS_ACTIVE, nullable = false)
    private Boolean isActive = true;

    @Column(name = UserFields.DELETED_TIME)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deletedTime;

}