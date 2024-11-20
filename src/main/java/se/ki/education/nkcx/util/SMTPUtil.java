package se.ki.education.nkcx.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class SMTPUtil {

    private final static Logger LOG = LogManager.getLogger();

    private final JavaMailSender javaMailSender;

    @Value("${sender.email}")
    private String senderEmail;

    @Value("${sender.email.name}")
    private String senderEmailName;

    @Autowired
    public SMTPUtil(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String message) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        if (senderEmail != null && !senderEmail.isEmpty()) {
            simpleMailMessage.setFrom(senderEmailName + " <" + senderEmail + ">");
        }
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        javaMailSender.send(simpleMailMessage);

        LOG.info("----- Email sent. -----");
    }

    @Async
    public void sendFormattedEmail(String to, String subject, String body) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            if (senderEmail != null && !senderEmail.isEmpty()) {
                mimeMessageHelper.setFrom(senderEmailName + " <" + senderEmail + ">");
            }
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(mimeMessage);
        LOG.info("----- Formatted Email sent. -----");
    }
}
