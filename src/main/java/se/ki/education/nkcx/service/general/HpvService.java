package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.HpvReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CellRes;
import se.ki.education.nkcx.dto.response.HpvRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface HpvService {

    HpvRes save(HpvReq hpvReq);

    PaginationRes<HpvRes> get(PaginationReq paginationReq);

    HpvRes update(HpvReq hpvReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<HpvRes> findHpvByName(String name);

}
