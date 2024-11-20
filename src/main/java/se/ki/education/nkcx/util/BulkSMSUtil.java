package se.ki.education.nkcx.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
public class BulkSMSUtil implements SMSUtil {

    private static final Logger LOG = LogManager.getLogger();

    private String TOKEN_ID;
    private String TOKEN_SECRET;

    @Value("${bulksms.base.url}")
    private String BASE_URL;


    @Override
    public boolean sendSMS(String to, String text) {

        LOG.info("----- Sending SMS to " + to + " -----");

        String authEncoded = Base64.getEncoder().encodeToString((TOKEN_ID + ":" + TOKEN_SECRET).getBytes());

        Message message = new Message();
        message.setTo(to).setBody(text);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization", "Basic " + authEncoded);

        HttpEntity<Message> request = new HttpEntity<>(message, httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, request, String.class, httpHeaders);
        return response.getBody() != null && response.getBody().contains("type") && response.getBody().contains("SENT");
    }

    class Message {
        private String to;
        private String body;

        public String getTo() {
            return to;
        }

        public Message setTo(String to) {
            this.to = to;
            return this;
        }

        public String getBody() {
            return body;
        }

        public Message setBody(String body) {
            this.body = body;
            return this;
        }
    }
}
