package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.ParishReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.ParishRes;

import java.io.IOException;
import java.util.List;

public interface ParishService {
    ParishRes save(ParishReq parishReq);

    PaginationRes<ParishRes> get(PaginationReq paginationReq);

    ParishRes update(ParishReq parishReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<ParishRes> findParishByName(String name);
}
