package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.ExtHpvReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.ExtHpvRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface ExtHpvService {
    ExtHpvRes save(ExtHpvReq extHpvReq);

    PaginationRes<ExtHpvRes> get(PaginationReq paginationReq);

    ExtHpvRes update(ExtHpvReq extHpvReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<ExtHpvRes> findExtHpvByName(String name);
}
