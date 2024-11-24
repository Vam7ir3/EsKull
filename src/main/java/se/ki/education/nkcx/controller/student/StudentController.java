package se.ki.education.nkcx.controller.student;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.ki.education.nkcx.dto.ServiceResponseUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.student.StudentReq;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.response.student.StudentRes;
import se.ki.education.nkcx.service.general.student.StudentService;
import se.ki.education.nkcx.service.general.student.StudentServiceImpl;

@RestController
@RequestMapping(value = "/api/student")
@Api(tags = "Student", description = "APIs related to Student.")
public class StudentController {
    private static final Logger LOG = LogManager.getLogger();
    private final StudentService studentService;
    private final ServiceResponseUtil<StudentRes> studentDtoServiceResponseUtil;
    private final StudentServiceImpl studentServiceImpl;
    private final ServiceResponseUtil serviceResponseUtil;

    public StudentController(StudentService studentService, ServiceResponseUtil<StudentRes> studentDtoServiceResponseUtil, StudentServiceImpl studentServiceImpl, ServiceResponseUtil serviceResponseUtil) {
        this.studentService = studentService;
        this.studentDtoServiceResponseUtil = studentDtoServiceResponseUtil;
        this.studentServiceImpl = studentServiceImpl;
        this.serviceResponseUtil = serviceResponseUtil;
    }

    @PostMapping
    @ApiOperation(value = "Save a new Student.")
    public ResponseEntity<ServiceRes> save(@RequestBody StudentReq studentReq) {
        LOG.info("Incoming request to save person: {}", studentReq);

        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Student saved.")
                        .addData("student", studentService.save(studentReq))
        );
    }

    @GetMapping
    @ApiOperation(value = "Get paginated list or all Student")
    public ResponseEntity<ServiceRes> getPaginated(PaginationReq pagination) {
        return ResponseEntity.ok(studentDtoServiceResponseUtil.buildServiceResponse(
                true,
                "Student retrieved",
                studentService.get(pagination)
        ));
    }

    @PutMapping
    @ApiOperation(value = "Update a new Student")
    public ResponseEntity<ServiceRes> update(@RequestBody StudentReq studentReq) {
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Student updated.")
                        .addData("student", studentService.update(studentReq))
        );
    }

    @DeleteMapping(value = "/{studentId}")
    @ApiOperation(value = "Delete an existing Student.")
    public ResponseEntity<ServiceRes> delete(@PathVariable Long studentId) {
        studentService.delete(studentId);
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Student Deleted")
        );
    }
}
