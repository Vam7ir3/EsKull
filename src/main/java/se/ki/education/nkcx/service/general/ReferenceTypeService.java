package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.InvitationTypeReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.ReferenceTypeReq;
import se.ki.education.nkcx.dto.response.InvitationTypeRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.ReferenceTypeRes;

import java.io.IOException;
import java.util.List;

public interface ReferenceTypeService {

    ReferenceTypeRes save(ReferenceTypeReq referenceTypeReq);

    PaginationRes<ReferenceTypeRes> get(PaginationReq paginationReq);

    ReferenceTypeRes update(ReferenceTypeReq referenceTypeReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<ReferenceTypeRes> findReferenceTypeByName(String type);
}
