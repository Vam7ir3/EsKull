package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.CountryReq;
import se.ki.education.nkcx.dto.response.CountryRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.CountryEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.CountryRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class CountryServiceImpl implements CountryService {

    private static final Logger LOG = LogManager.getLogger();

    private final CountryRepo countryRepo;
    private final DtoUtil<CountryEntity, CountryReq, CountryRes> dtoUtil;
    private final PaginationDtoUtil<CountryEntity, CountryReq, CountryRes> paginationDtoUtil;

    @Autowired
    public CountryServiceImpl(CountryRepo countryRepo, DtoUtil<CountryEntity, CountryReq, CountryRes> dtoUtil, PaginationDtoUtil<CountryEntity, CountryReq, CountryRes> paginationDtoUtil) {
        this.countryRepo = countryRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('COUNTRY_C')")
    @Override
    public CountryRes save(CountryReq countryReq) {
        LOG.info("----- Saving Country. -----");

        countryRepo.findByName(countryReq.getName()).ifPresent((countryEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.COU002.getMessage());
        }));

        return dtoUtil.prepRes(countryRepo.save(dtoUtil.reqToEntity(countryReq)));
    }

    @Override
    public PaginationRes<CountryRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting paginated Country list. -----");

        List<String> fields = Arrays.asList("name", "dialCode", "code");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, countryRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('COUNTRY_U')")
    @Override
    public CountryRes update(CountryReq countryReq) {
        LOG.info("----- Updating Country. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(countryReq.getId());
        optionalCountryEntity.orElseThrow(() -> new CustomException("COU001"));
        CountryEntity countryEntity = optionalCountryEntity.get();

        dtoUtil.setUpdatedValue(countryReq, countryEntity);
        return dtoUtil.prepRes(countryEntity);
    }

    @PreAuthorize("hasAuthority('COUNTRY_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Country. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(id);
        optionalCountryEntity.orElseThrow(() -> new CustomException("COU001"));

        countryRepo.delete(optionalCountryEntity.get());
    }
}
