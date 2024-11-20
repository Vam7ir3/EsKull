package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.LaboratoryReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.LaboratoryRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface LaboratoryService {
    LaboratoryRes save(LaboratoryReq laboratoryReq);

    PaginationRes<LaboratoryRes> get(PaginationReq paginationReq);

    LaboratoryRes update(LaboratoryReq laboratoryReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<LaboratoryRes> findLaboratoryByName(String name);

    List<LaboratoryRes> filterIsInUse(Boolean isInUse);
}
