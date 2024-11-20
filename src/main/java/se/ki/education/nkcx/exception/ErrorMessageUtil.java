package se.ki.education.nkcx.exception;

import se.ki.education.nkcx.entity.MultiLangMessageEntity;
import se.ki.education.nkcx.enums.LanguageCode;
import se.ki.education.nkcx.repo.MultiLangMessageRepo;
import se.ki.education.nkcx.util.CommonValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorMessageUtil {

    private final MultiLangMessageRepo multiLangMessageRepo;
    private final CommonValue commonValue;

    @Autowired
    public ErrorMessageUtil(MultiLangMessageRepo multiLangMessageRepo, CommonValue commonValue) {
        this.multiLangMessageRepo = multiLangMessageRepo;
        this.commonValue = commonValue;
    }

    public String getMessage(String code) {
        MultiLangMessageEntity multiLangMessageEntity = multiLangMessageRepo.findByCode(code).orElse(new MultiLangMessageEntity());
        if (commonValue.getClientLanguageCode().equals(LanguageCode.SWEDISH)) {
            return multiLangMessageEntity.getSwedish() != null ? multiLangMessageEntity.getSwedish() : "";
        } else if (commonValue.getClientLanguageCode().equals(LanguageCode.SPANISH)) {
            return multiLangMessageEntity.getSpanish() != null ? multiLangMessageEntity.getSpanish() : "";
        } else {
            return multiLangMessageEntity.getEnglish() != null ? multiLangMessageEntity.getEnglish() : "Error Message not found.";
        }
    }
}
