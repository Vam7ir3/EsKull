package se.ki.education.nkcx.service.general;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import se.ki.education.nkcx.dto.request.CountyLabReq;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.CountyLabRes;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.util.CountyLabDtoUtil;
import se.ki.education.nkcx.entity.CountyLabEntity;
import se.ki.education.nkcx.entity.PersonSampleEntity;
import se.ki.education.nkcx.repo.CountryRepo;
import se.ki.education.nkcx.repo.CountyLabRepo;
import se.ki.education.nkcx.repo.CountyRepo;
import se.ki.education.nkcx.repo.LabRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountyLabServiceImpl implements CountyLabService {

    @Autowired
    private CountyLabRepo countyLabRepo;

    @Autowired
    private CountyRepo countyRepo;

    @Autowired
    private LabRepo labRepo;

    @Autowired
    private CountyLabDtoUtil dtoUtil;

    @Override
    public CountyLabRes getById(Long id) {
        CountyLabEntity countyLabEntity = countyLabRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("County Lab not found"));
        return dtoUtil.entityToRes(countyLabEntity);
    }

    @Override
    public List<CountyLabRes> getAll() {
        List<CountyLabEntity> countyLabEntities = countyLabRepo.findAll();
        return countyLabEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<CountyLabRes> filterByCounty(List<Long> countyIds) {
        return List.of();
    }

    @Override
    public List<CountyLabRes> searchByLabName(String name) {
        return List.of();
    }

    @Override
    public CountyLabRes saveCountyLab(CountyLabReq countyLabReq) {
        CountyLabEntity countyLabEntity = new CountyLabEntity();
        countyLabEntity.setCountyEntity(countyRepo.findById(countyLabReq.getCountyId())
                .orElseThrow(() -> new RuntimeException("County not found")));
        countyLabEntity.setLabEntity(labRepo.findById(countyLabReq.getLabId())
                .orElseThrow(() -> new RuntimeException("Lab not found")));

        CountyLabEntity savedEntity = countyLabRepo.save(countyLabEntity);
        return dtoUtil.entityToRes(savedEntity);
    }

//    @Override
//    public PaginationRes<CountyLabRes> getByCounty(PaginationReq paginationReq, Long countyId) {
//        String sortBy = "createdDate";
//        Sort.Direction sortOrder = Sort.Direction.DESC;
//    }
}

