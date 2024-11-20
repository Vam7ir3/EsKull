package se.ki.education.nkcx.service;

public interface EmailService {
    void sendFormattedEmail(String to, String subject, String body);
}
