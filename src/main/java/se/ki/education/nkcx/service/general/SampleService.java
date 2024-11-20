package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.SampleReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CellRes;
import se.ki.education.nkcx.dto.response.SampleRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface SampleService {

    SampleRes save(SampleReq sampleReq);

    PaginationRes<SampleRes> get(PaginationReq paginationReq);

    SampleRes update(SampleReq sampleReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<SampleRes> findSampleByName(String type);

}
