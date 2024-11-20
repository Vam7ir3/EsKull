package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.Cell6923Req;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.Cell6923Res;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;

public interface Cell6923Service {
    Cell6923Res save(Cell6923Req cell6923Req);

    PaginationRes<Cell6923Res> get(PaginationReq paginationReq);

    Cell6923Res update(Cell6923Req cell6923Req);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

//    List<Cell6923Res> findCell6923ByName(String district);
}
