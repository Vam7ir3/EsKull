package se.ki.education.nkcx.util;

import se.ki.education.nkcx.enums.LanguageCode;
import org.springframework.stereotype.Component;

@Component
public class CommonValue {
    private LanguageCode clientLanguageCode = LanguageCode.ENGLISH;

    public LanguageCode getClientLanguageCode() {
        return clientLanguageCode;
    }

    public void setClientLanguageCode(LanguageCode clientLanguageCode) {
        this.clientLanguageCode = clientLanguageCode;
    }
}
