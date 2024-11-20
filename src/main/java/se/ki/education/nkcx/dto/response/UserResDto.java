package se.ki.education.nkcx.dto.response;

import se.ki.education.nkcx.enums.Gender;
import se.ki.education.nkcx.util.LocalDateSerializer;
import se.ki.education.nkcx.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;

    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;

    private String mobileNumber;
    private String emailAddress;
    private String imageUrl;
    private RoleResDto role;

    private boolean hasAnsweredQuestionnaire;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdDateTime;

    public UserResDto setId(Long id) {
        this.id = id;
        return this;
    }

    public UserResDto setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserResDto setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserResDto setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public UserResDto setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public UserResDto setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
        return this;
    }

    public UserResDto setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserResDto setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public UserResDto setRole(RoleResDto role) {
        this.role = role;
        return this;
    }

    public UserResDto setHasAnsweredQuestionnaire(boolean hasAnsweredQuestionnaire) {
        this.hasAnsweredQuestionnaire = hasAnsweredQuestionnaire;
        return this;
    }

    public UserResDto setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
        return this;
    }

    @JsonIgnore
    private List<AuthorityRes> authorities;
}
