package se.ki.education.nkcx.service.general.student;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.student.StudentReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.student.StudentRes;

import java.io.IOException;

public interface StudentService {

    StudentRes save(StudentReq studentReq);

    PaginationRes<StudentRes> get(PaginationReq paginationReq);

    StudentRes update(StudentReq studentReq);

    void delete(Long id);

    Boolean importData(MultipartFile multipartFile) throws IOException;

    Workbook exportFile();

    PaginationRes<StudentRes> filterStudent(PaginationReq paginationReq, String grade);
}


