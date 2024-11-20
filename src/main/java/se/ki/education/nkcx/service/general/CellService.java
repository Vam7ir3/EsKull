package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.CellReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CellRes;
import se.ki.education.nkcx.dto.response.CellRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface CellService {

    CellRes save(CellReq cellReq);

    PaginationRes<CellRes> get(PaginationReq paginationReq);

    CellRes update(CellReq cellReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<CellRes> findCellByName(String name);

}
