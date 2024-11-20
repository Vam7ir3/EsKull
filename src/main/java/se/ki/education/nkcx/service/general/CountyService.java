package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.CountyReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CountyRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface CountyService {
    CountyRes save(CountyReq countyReq);

    PaginationRes<CountyRes> get(PaginationReq paginationReq);

    CountyRes update(CountyReq countyReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<CountyRes> findCountyByName(String name);
}
