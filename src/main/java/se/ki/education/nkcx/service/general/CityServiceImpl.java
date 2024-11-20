package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.CityReq;
import se.ki.education.nkcx.dto.response.CityRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.CityEntity;
import se.ki.education.nkcx.entity.StateEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.repo.CityRepo;
import se.ki.education.nkcx.repo.StateRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CityServiceImpl implements CityService {

    private static final Logger LOG = LogManager.getLogger();

    private final CityRepo cityRepo;
    private final StateRepo stateRepo;
    private final DtoUtil<CityEntity, CityReq, CityRes> dtoUtil;
    private final PaginationDtoUtil<CityEntity, CityReq, CityRes> paginationDtoUtil;

    @Autowired
    public CityServiceImpl(CityRepo cityRepo, StateRepo stateRepo, DtoUtil<CityEntity, CityReq, CityRes> dtoUtil, PaginationDtoUtil<CityEntity, CityReq, CityRes> paginationDtoUtil) {
        this.cityRepo = cityRepo;
        this.stateRepo = stateRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('CITY_C')")
    @Override
    public CityRes save(CityReq cityReq) {
        LOG.info("----- Saving City. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(cityReq.getStateId());
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        cityRepo.findByNameAndState(cityReq.getName(), optionalStateEntity.get()).ifPresent((stateEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.CIT001.getMessage());
        }));

        CityEntity city = dtoUtil.reqToEntity(cityReq);
        city.setState(optionalStateEntity.get());

        return dtoUtil.prepRes(cityRepo.save(city));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginationRes<CityRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting paginated City list. -----");

        List<String> fields = Arrays.asList("name");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, cityRepo, dtoUtil);
    }

    @Override
    public PaginationRes<CityRes> getByState(Long stateId, PaginationReq paginationReq) {
        LOG.info("----- Getting City by State. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(stateId);
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        List<String> fields = Arrays.asList("name");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        if (paginationReq.getSortBy() != null && fields.contains(paginationReq.getSortBy())
                && paginationReq.getSortOrder() != null) {
            sortBy = paginationReq.getSortBy();
            sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<CityEntity> cityEntityPage = null;
        List<CityEntity> cityEntities;
        if (paginationReq.getPageSize() > 0) {
            cityEntityPage = cityRepo.findAllByState(optionalStateEntity.get(), PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), sortOrder, sortBy));
            cityEntities = cityEntityPage.getContent();
        } else {
            cityEntities = cityRepo.findAllByState(optionalStateEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<CityRes> cityRes = cityEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(cityEntityPage, cityRes);
    }

    @PreAuthorize("hasAuthority('CITY_U')")
    @Override
    public CityRes update(CityReq cityReq) {
        LOG.info("----- Updating City. -----");

        Optional<CityEntity> optionalCityEntity = cityRepo.findById(cityReq.getId());
        optionalCityEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.CIT002.getMessage()));

        if (cityReq.getStateId() != null) {
            Optional<StateEntity> optionalStateEntity = stateRepo.findById(cityReq.getStateId());
            optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));
            optionalCityEntity.get().setState(optionalStateEntity.get());
        }

        dtoUtil.setUpdatedValue(cityReq, optionalCityEntity.get());
        return dtoUtil.prepRes(optionalCityEntity.get());
    }

    @PreAuthorize("hasAuthority('CITY_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting City. -----");

        Optional<CityEntity> optionalCityEntity = cityRepo.findById(id);
        optionalCityEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.CIT002.getMessage()));

        if (optionalCityEntity.get().getAddresses() != null && optionalCityEntity.get().getAddresses().size() > 0) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, ErrorMessage.CIT003.getMessage());
        }

        cityRepo.delete(optionalCityEntity.get());
    }
}
