package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.StateReq;
import se.ki.education.nkcx.dto.response.StateResDto;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.CountryEntity;
import se.ki.education.nkcx.entity.StateEntity;
import se.ki.education.nkcx.enums.ErrorMessage;
import se.ki.education.nkcx.repo.CountryRepo;
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
public class StateServiceImpl implements StateService {

    private static final Logger LOG = LogManager.getLogger();

    private final StateRepo stateRepo;
    private final CountryRepo countryRepo;
    private final DtoUtil<StateEntity, StateReq, StateResDto> dtoUtil;
    private final PaginationDtoUtil<StateEntity, StateReq, StateResDto> paginationDtoUtil;

    @Autowired
    public StateServiceImpl(StateRepo stateRepo, CountryRepo countryRepo, DtoUtil<StateEntity, StateReq, StateResDto> dtoUtil, PaginationDtoUtil<StateEntity, StateReq, StateResDto> paginationDtoUtil) {
        this.stateRepo = stateRepo;
        this.countryRepo = countryRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('STATE_C')")
    @Override
    public StateResDto save(StateReq stateReq) {
        LOG.info("----- Saving State. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(stateReq.getCountryId());
        optionalCountryEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.COU001.getMessage()));

        stateRepo.findByNameAndCountry(stateReq.getName(), optionalCountryEntity.get()).ifPresent((stateEntity -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, ErrorMessage.STA001.getMessage());
        }));

        StateEntity state = dtoUtil.reqToEntity(stateReq);
        state.setCountry(optionalCountryEntity.get());

        return dtoUtil.prepRes(stateRepo.save(state));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginationRes<StateResDto> get(PaginationReq paginationReq) {
        LOG.info("----- Getting paginated State list. -----");

        List<String> fields = Arrays.asList("name", "code");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, stateRepo, dtoUtil);
    }

    @Override
    public PaginationRes<StateResDto> getByCountry(Long countryId, PaginationReq paginationReq) {
        LOG.info("----- Getting State by Country. -----");

        Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(countryId);
        optionalCountryEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.COU001.getMessage()));

        List<String> fields = Arrays.asList("name", "code");
        String sortBy = "name";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        if (paginationReq.getSortBy() != null && fields.contains(paginationReq.getSortBy())
                && paginationReq.getSortOrder() != null) {
            sortBy = paginationReq.getSortBy();
            sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<StateEntity> stateEntityPage = null;
        List<StateEntity> stateEntities;
        if (paginationReq.getPageSize() > 0) {
            stateEntityPage = stateRepo.findAllByCountry(optionalCountryEntity.get(), PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), sortOrder, sortBy));
            stateEntities = stateEntityPage.getContent();
        } else {
            stateEntities = stateRepo.findAllByCountry(optionalCountryEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<StateResDto> stateResDtos = stateEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(stateEntityPage, stateResDtos);
    }

    @PreAuthorize("hasAuthority('STATE_U')")
    @Override
    public StateResDto update(StateReq stateReq) {
        LOG.info("----- Updating State. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(stateReq.getId());
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        if (stateReq.getCountryId() != null) {
            Optional<CountryEntity> optionalCountryEntity = countryRepo.findById(stateReq.getCountryId());
            optionalCountryEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.COU001.getMessage()));
            optionalStateEntity.get().setCountry(optionalCountryEntity.get());
        }

        dtoUtil.setUpdatedValue(stateReq, optionalStateEntity.get());
        return dtoUtil.prepRes(optionalStateEntity.get());
    }

    @PreAuthorize("hasAuthority('STATE_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting State. -----");

        Optional<StateEntity> optionalStateEntity = stateRepo.findById(id);
        optionalStateEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.STA002.getMessage()));

        stateRepo.delete(optionalStateEntity.get());
    }
}
