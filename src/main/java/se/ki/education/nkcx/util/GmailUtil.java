package se.ki.education.nkcx.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@Service
public class GmailUtil {

    private final static Logger LOG = LogManager.getLogger();
    private final GmailTokenHolder gmailTokenHolder;
    @Value("${sender.gmail}")
    private String senderGmail;
    @Value("${gmail.auth.url}")
    private String gmailAuthUrl;
    @Value("${gmail.app.name}")
    private String appName;
    @Value("${gmail.client.id}")
    private String clientId;
    @Value("${gmail.client.secret}")
    private String clientSecret;
    @Value("${gmail.access.token}")
    private String accessToken;
    @Value("${gmail.refresh.token}")
    private String refreshToken;
    @Value("${gmail.sending.base.url}")
    private String gmailSendingBaseUrl;

    public GmailUtil(GmailTokenHolder gmailTokenHolder) {
        this.gmailTokenHolder = gmailTokenHolder;
    }

    @Async
    public void sendFormattedEmail(String to, String subject, String body) {
        try {
            boolean emailSent = sendMessage(to, subject, body);
            if (emailSent) {
                LOG.info("----- Gmail Sent. -----");
            } else {
                LOG.error("***** Gmail not sent. *****");
            }
        } catch (MessagingException e) {
            LOG.error("***** Gmail not sent. *****");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("***** Gmail not sent. *****");
            e.printStackTrace();
        }
    }

    @Async
    public void sendGmail(List<String> recipients, String subject, String body, File file) {
        try {
            boolean emailSent = sendMessage(recipients, subject, body, file);
            if (emailSent) {
                LOG.info("----- Gmail Sent with Attachment. -----");
            } else {
                LOG.error("***** Gmail not sent with Attachment. *****");
            }
        } catch (MessagingException e) {
            LOG.error("***** Gmail not sent with Attachment. *****");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("***** Gmail not sent with Attachment. *****");
            e.printStackTrace();
        }
    }

    private boolean sendMessage(String recipientAddress, String subject, String body) throws MessagingException,
            IOException {
        updateAccessToken();
        String encodedEmail = createEmail(recipientAddress, senderGmail, subject, body);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", gmailTokenHolder.getTokenType() + " " + gmailTokenHolder.getAccessToken());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("raw", encodedEmail);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(gmailSendingBaseUrl + senderGmail + "/messages/send",
                HttpMethod.POST, request, String.class, httpHeaders);

        return response.getStatusCode().is2xxSuccessful();
    }

    private boolean sendMessage(List<String> recipientAddresses, String subject, String body, File file) throws MessagingException,
            IOException {
        updateAccessToken();
        String encodedEmail = createEmail(recipientAddresses, senderGmail, subject, body, file);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", gmailTokenHolder.getTokenType() + " " + gmailTokenHolder.getAccessToken());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("raw", encodedEmail);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(gmailSendingBaseUrl + senderGmail + "/messages/send",
                HttpMethod.POST, request, String.class, httpHeaders);

        return response.getStatusCode().is2xxSuccessful();
    }

    private void updateAccessToken() throws IOException {
        if (gmailTokenHolder.getAccessToken() == null ||
                gmailTokenHolder.getExpireAt().isBefore(LocalDateTime.now())) {
            TokenResponseDto tokenResponseDto = getAccessTokenFromRefreshToken();
            gmailTokenHolder.setAccessToken(tokenResponseDto.getAccess_token());
            gmailTokenHolder.setExpireAt(LocalDateTime.now().plusSeconds(Long.parseLong(tokenResponseDto.getExpires_in())));
            gmailTokenHolder.setScope(tokenResponseDto.getScope());
            gmailTokenHolder.setTokenType(tokenResponseDto.getToken_type());
        }
    }

    private TokenResponseDto getAccessTokenFromRefreshToken() throws IOException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(gmailAuthUrl, HttpMethod.POST, request, String.class, httpHeaders);

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), TokenResponseDto.class);
    }

    private String createEmail(String to, String from, String subject, String bodyText) throws MessagingException, IOException {
        MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setContent(bodyText, "text/html");
        return encodeEmail(email);
    }

    private String createEmail(List<String> to, String from, String subject, String bodyText, File file) throws MessagingException, IOException {
        MimeMessage email = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        email.setFrom(new InternetAddress(from));
        InternetAddress[] toAddresses = new InternetAddress[to.size()];
        for (int i = 0; i < to.size(); i++) {
            toAddresses[i] = new InternetAddress(to.get(i));
        }
        email.addRecipients(javax.mail.Message.RecipientType.TO, toAddresses);
        email.setSubject(subject);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(bodyText, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        mimeBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(file);

        mimeBodyPart.setDataHandler(new DataHandler(source));
        mimeBodyPart.setFileName(file.getName());

        multipart.addBodyPart(mimeBodyPart);

        email.setContent(multipart);
        return encodeEmail(email);
    }

    private String encodeEmail(MimeMessage emailContent) throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);

        return Base64.encodeBase64URLSafeString(buffer.toByteArray());
    }
}
