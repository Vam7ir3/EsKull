package se.ki.education.nkcx.service.general;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import se.ki.education.nkcx.dto.request.Cell6923Req;
import se.ki.education.nkcx.dto.request.DistrictReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.request.PersonSampleReq;
import se.ki.education.nkcx.dto.response.Cell6923Res;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.response.PersonRes;
import se.ki.education.nkcx.dto.response.PersonSampleRes;
import se.ki.education.nkcx.dto.util.Cell6923DtoUtil;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.entity.*;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.*;
import se.ki.education.nkcx.util.DataValidationForExcelImport;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class Cell6923ServiceImpl implements Cell6923Service {

    private static final Logger LOG = LogManager.getLogger();

    private final Cell6923Repo cell6923Repo;

    private final PersonRepo personRepo;

    private final LaboratoryRepo laboratoryRepo;

    private final CountyRepo countyRepo;

    private final ReferenceTypeRepo referenceTypeRepo;

    private final LogService logService;

    private final Cell6923DtoUtil dtoUtil;

    private final PaginationDtoUtil<Cell6923Entity, Cell6923Req, Cell6923Res> paginationDtoUtil;

    public Cell6923ServiceImpl(Cell6923Repo cell6923Repo, PersonRepo personRepo, LaboratoryRepo laboratoryRepo, CountyRepo countyRepo, ReferenceTypeRepo referenceTypeRepo, LogService logService, Cell6923DtoUtil dtoUtil, PaginationDtoUtil<Cell6923Entity, Cell6923Req, Cell6923Res> paginationDtoUtil) {
        this.cell6923Repo = cell6923Repo;
        this.personRepo = personRepo;
        this.laboratoryRepo = laboratoryRepo;
        this.countyRepo = countyRepo;
        this.referenceTypeRepo = referenceTypeRepo;
        this.logService = logService;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }


    @Override
    public Cell6923Res save(Cell6923Req cell6923Req) {
        LOG.info("----- Saving Cell6923. -----");

        PersonEntity existingPersonEntity = personRepo.findById(cell6923Req.getPersonId()).orElseThrow(null);
        LOG.info("Attempting to find entity with ID: {}", existingPersonEntity);
        LaboratoryEntity existingLaboratoryEntity = laboratoryRepo.findById(cell6923Req.getLaboratoryId()).orElseThrow(null);
        LOG.info("Attempting to find entity with ID: {}", existingLaboratoryEntity);
        CountyEntity existingCountyEntity = countyRepo.findById(cell6923Req.getCountyId()).orElseThrow(null);
        LOG.info("Attempting to find entity with ID: {}", existingCountyEntity);
//        ReferenceTypeEntity referenceTypeEntity = referenceTypeRepo.findById(cell6923Req.getReferralTypeId()).orElseThrow(null);
        ReferenceTypeEntity referenceTypeEntity = referenceTypeRepo.findById(cell6923Req.getReferralTypeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("ReferenceType not found with ID: %d", cell6923Req.getReferralTypeId())
                ));
        LOG.info("Found ReferenceType entity with ID: {}", referenceTypeEntity.getId());


        Cell6923Entity cell6923Entity = dtoUtil.reqToEntity(cell6923Req);
        cell6923Entity.setPersonEntity(existingPersonEntity);
        cell6923Entity.setLaboratoryEntity(existingLaboratoryEntity);
        cell6923Entity.setCountyEntity(existingCountyEntity);
        cell6923Entity.setReferenceTypeEntity(referenceTypeEntity);

        Cell6923Entity savedEntity = cell6923Repo.save(cell6923Entity);
        Cell6923Res res = dtoUtil.prepRes(savedEntity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Added new Cell", "A new Cell data is added successfully. ",  currentUser);

        return  res;
    }

    @Override
    public PaginationRes<Cell6923Res> get(PaginationReq paginationReq) {
        LOG.info("----- Getting Cell6923. -----");

        List<String> validFields = Arrays.asList("id");
        String sortBy = paginationReq.getSortBy();
        if (!validFields.contains(sortBy)) {
            LOG.warn("Invalid sort field: {}. Defaulting to 'id'", sortBy);
            sortBy = "id";  // Default sortBy
        }

        Sort.Direction sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), Sort.by(sortOrder, sortBy));
        Page<Cell6923Entity> cell6923EntityPage = cell6923Repo.findAll(pageable);

        if ("id".equals(sortBy)) {
            cell6923EntityPage = cell6923Repo.findAll(pageable);
        } else {
            cell6923EntityPage = cell6923Repo.findAll(pageable);
        }
        List<Cell6923Res> cell6923Res = cell6923EntityPage.getContent().stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());


        return paginationDtoUtil.prepPaginationDto(cell6923EntityPage, cell6923Res);
    }

    @Override
    public Cell6923Res update(Cell6923Req cell6923Req) {
        LOG.info("----- Updating Cell6923. -----");
        Optional<Cell6923Entity> optionalCell6923Entity = cell6923Repo.findById(cell6923Req.getId());
        optionalCell6923Entity.orElseThrow(() -> new CustomException("COU001"));
        Cell6923Entity cell6923Entity = optionalCell6923Entity.get();
        dtoUtil.setUpdatedValue(cell6923Req, cell6923Entity);

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Editing existing Cell6923 data", "Existing Cell6923 data is modified successfully.", currentUser);

        return dtoUtil.prepRes(cell6923Entity);
    }


    @Override
    public void delete(Long id) {

        LOG.info("----- Deleting Cell6923. -----");
        Optional<Cell6923Entity> optionalCell6923Entity = cell6923Repo.findById(id);
        optionalCell6923Entity.orElseThrow(() -> new CustomException("COU001"));
        cell6923Repo.delete(optionalCell6923Entity.get());

        UserEntity currentUser = logService.getCurrentUser();
        logService.logActivity("Deleted existing Cell6923", "Existing Cell6923 data is deleted successfully. ",  currentUser);

    }

    @Override
    public Boolean importData(MultipartFile multipartFile) throws IOException {
        LOG.info("---- Importing Cell6923 Data. ------");

        List<Cell6923Req> cell6923Reqs = extractDataFromFile(multipartFile.getInputStream());

        List<Cell6923Entity> cell6923Entities = new ArrayList<>();
        for (Cell6923Req cell6923Req : cell6923Reqs) {
            Cell6923Entity cell6923Entity = dtoUtil.reqToEntity(cell6923Req);
            cell6923Entities.add(cell6923Entity);

        }
        return cell6923Repo.saveAll(cell6923Entities).size() > 0;
    }

    private List<Cell6923Req> extractDataFromFile(InputStream inputStream) throws IOException {
        Workbook workbook = WorkbookFactory.create(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        List<Cell6923Req> cell6923Reqs = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Cell6923Req cell6923Req = extractDataFromRow(row);
            cell6923Reqs.add(cell6923Req);
        }

        workbook.close();
        return cell6923Reqs;
    }

    private Cell6923Req extractDataFromRow(Row row) {
        Cell6923Req cell6923Req = new Cell6923Req();

        cell6923Req.setPersonId(Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 0)));
        cell6923Req.setLaboratoryId(Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 1)));
        cell6923Req.setCountyId(Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 2)));
        cell6923Req.setSampleDate(DataValidationForExcelImport.getCellValueOrDefault(row, 3));
//        cell6923Req.setSampleType(DataValidationForExcelImport.getCellValueOrDefault(row, 4));
//        cell6923Req.setReferralNumber(Math.toIntExact(Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 5))));
//        cell6923Req.setReferenceTypeId(Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 6)));
//        cell6923Req.setReferenceSite(DataValidationForExcelImport.getCellValueOrDefault(row, 7));
//        cell6923Req.setResidc(Math.toIntExact(Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 8))));
//        cell6923Req.setResidk(Math.toIntExact(Long.valueOf(DataValidationForExcelImport.getCellValueOrDefault(row, 9))));


        return cell6923Req;
    }

    @Override
    public Workbook exportFile() {
        return null;
    }
}
