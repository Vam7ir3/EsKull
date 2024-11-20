package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.DistrictReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.DistrictRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface DistrictService {

    DistrictRes save(DistrictReq districtReq);

    PaginationRes<DistrictRes> get(PaginationReq paginationReq);

    DistrictRes update(DistrictReq districtReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<DistrictRes> findDistrictByName(String district);
}
