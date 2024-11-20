package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.MunicipalityReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.MunicipalityRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface MunicipalityService {

    MunicipalityRes save(MunicipalityReq municipalityReq);

    PaginationRes<MunicipalityRes> get(PaginationReq paginationReq);

    MunicipalityRes update(MunicipalityReq municipalityReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<MunicipalityRes> findMunicipalityByName(String name);
}
