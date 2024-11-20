package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.InvitationTypeReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.InvitationTypeRes;
import se.ki.education.nkcx.dto.response.PaginationRes;

import java.io.IOException;
import java.util.List;

public interface InvitationTypeService {

    InvitationTypeRes save(InvitationTypeReq invitationTypeReq);

    PaginationRes<InvitationTypeRes> get(PaginationReq paginationReq);

    InvitationTypeRes update(InvitationTypeReq invitationTypeReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    List<InvitationTypeRes> findInvitationTypeByName(String type);
}
