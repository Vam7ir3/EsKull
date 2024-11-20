package se.ki.education.nkcx.service.general;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.PersonReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonRes;

import java.io.IOException;

public interface PersonService {
    PersonRes save(PersonReq personReq);

    PaginationRes<PersonRes> get(PaginationReq paginationReq);

    PersonRes update(PersonReq personReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

//    List<PersonRes> findPersonByEmail(String email);

//    List<PersonRes> filterPersonByIsValidPnr(Boolean isValidPNR);

    PaginationRes<PersonRes> filterPerson(PaginationReq paginationReq, Boolean isValidPNR);

}