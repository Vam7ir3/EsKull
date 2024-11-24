package se.ki.education.nkcx.service.general.student;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.student.StudentReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.student.StudentRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.entity.student.StudentEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.student.StudentRepo;
import se.ki.education.nkcx.service.general.LogService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class StudentServiceImpl implements StudentService {

    private static final Logger LOG = LogManager.getLogger();

    private LogService logService;

    private final StudentRepo studentRepo;

    private final DtoUtil<StudentEntity, StudentReq, StudentRes> dtoUtil;

    private final PaginationDtoUtil<StudentEntity, StudentReq, StudentRes> paginationDtoUtil;

    @Autowired
    public StudentServiceImpl(LogService logService, StudentRepo studentRepo, DtoUtil<StudentEntity, StudentReq, StudentRes> dtoUtil, PaginationDtoUtil<StudentEntity, StudentReq, StudentRes> paginationDtoUtil) {
        this.logService = logService;
        this.studentRepo = studentRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }


    @PreAuthorize("hasAuthority('STUDENT_C')")
    @Override
    public StudentRes save(StudentReq studentReq) {
        LOG.info("----- Saving Student. -----");
        studentRepo.findByFirstName(studentReq.getBatchNumber()).ifPresent(personEntity -> {
            throw  new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.PERSON002.getMessage());
        });
        StudentEntity entity = dtoUtil.reqToEntity(studentReq);
        StudentEntity savedEntity =  studentRepo.save(entity);
        StudentRes res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Student", "A new student data is added successfully. ",  currentUser);


        return  res;
    }

    @Override
    public PaginationRes<StudentRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting Student. -----");

        List<String> validFields = Arrays.asList("batchNumber");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'firstName'", sortBy);
            sortBy = "firstName";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<StudentEntity> studentEntityPage = studentRepo.findAll(pageable);

        if ("batchNumber".equals(sortBy)) {
            studentEntityPage = studentRepo.findAll(pageable);
        } else {
            studentEntityPage = studentRepo.findAll(pageable);
        }
        List<StudentRes> studentRes = studentEntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(studentEntityPage, studentRes);
    }

    @PreAuthorize("hasAuthority('STUDENT_U')")
    @Override
    public StudentRes update(StudentReq studentReq) {
        LOG.info("----- Updating Student. -----");
        Optional<StudentEntity> optionalStudentEntity = studentRepo.findById(studentReq.getId());
        optionalStudentEntity.orElseThrow(() -> new CustomException("COU001"));
        StudentEntity studentEntity = optionalStudentEntity.get();
        dtoUtil.setUpdatedValue(studentReq, studentEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing student data", "Existing student data is modified successfully.", currentUser);

        return dtoUtil.prepRes(studentEntity);
    }

    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Person. -----");
        Optional<StudentEntity> optionalStudentEntity = studentRepo.findById(id);
        optionalStudentEntity.orElseThrow(() -> new CustomException("COU001"));
        studentRepo.delete(optionalStudentEntity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing student", "Existing student data is deleted successfully. ",  currentUser);
    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        return null;
    }

    @Override
    public Workbook exportFile() {
        return null;
    }

    @Override
    public PaginationRes<StudentRes> filterStudent(PaginationReq paginationReq, String grade) {
        return null;
    }
}
