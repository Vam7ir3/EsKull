package se.ki.education.nkcx;

import se.ki.education.nkcx.util.GmailTokenHolder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "utcDateTimeProvider")
@EnableScheduling
public class NKCXApplication {

    public static void main(String[] args) {
        SpringApplication.run(NKCXApplication.class, args);
    }

    @Bean
    public GmailTokenHolder gmailTokenHolder() {
        return new GmailTokenHolder();
    }

    @Bean
    public DateTimeProvider utcDateTimeProvider() {
        return () -> Optional.of(LocalDateTime.now(ZoneOffset.UTC));
    }
}
