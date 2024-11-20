package se.ki.education.nkcx.dto.util;

import org.springframework.stereotype.Component;
import se.ki.education.nkcx.dto.request.KlartextReq;
import se.ki.education.nkcx.dto.response.KlartextRes;
import se.ki.education.nkcx.entity.KlartextEntity;

@Component
public class KlartextDtoUtil implements DtoUtil<KlartextEntity, KlartextReq, KlartextRes>{
    @Override
    public KlartextEntity reqToEntity(KlartextReq klartextReq) {
        return new KlartextEntity()
                .setSnomedText(klartextReq.getSnomedText());
    }

    @Override
    public KlartextRes entityToRes(KlartextEntity klartextEntity) {
        return new KlartextRes()
                .setId(klartextEntity.getId())
                .setSnomedText(klartextEntity.getSnomedText());
    }

    @Override
    public KlartextRes prepRes(KlartextEntity klartextEntity) {
        return entityToRes(klartextEntity);
    }

    @Override
    public void setUpdatedValue(KlartextReq klartextReq, KlartextEntity klartextEntity) {
        if (klartextReq != null && klartextEntity != null) {
            if (klartextEntity.getSnomedText() != null && !klartextReq.getSnomedText().equals(klartextEntity.getSnomedText())) {
                klartextEntity.setSnomedText(klartextReq.getSnomedText());
            }
        }
    }
}
