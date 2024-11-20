package se.ki.education.nkcx.repo.projection;

public interface UserProjection {
    Long getId();

    String getFirstName();

    String getLastName();

    String getEmailAddress();

    String getMobileNumber();
}
