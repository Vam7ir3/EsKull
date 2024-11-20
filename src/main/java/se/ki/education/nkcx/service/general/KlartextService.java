package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.DistrictReq;
import se.ki.education.nkcx.dto.request.KlartextReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.DistrictRes;
import se.ki.education.nkcx.dto.response.KlartextRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface KlartextService {

    KlartextRes save(KlartextReq klartextReq);

    PaginationRes<KlartextRes> get(PaginationReq paginationReq);

    KlartextRes update(KlartextReq klartextReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<KlartextRes> findKlartextByName(String snomedText);
}
