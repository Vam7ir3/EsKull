package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.AuthorityReq;
import se.ki.education.nkcx.dto.response.AuthorityRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.AuthorityEntity;
import se.ki.education.nkcx.repo.AuthorityRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthorityServiceImpl implements AuthorityService {

    private static final Logger LOG = LogManager.getLogger();

    private final AuthorityRepo authorityRepo;
    private final DtoUtil<AuthorityEntity, AuthorityReq, AuthorityRes> dtoUtil;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepo authorityRepo, DtoUtil<AuthorityEntity, AuthorityReq, AuthorityRes> dtoUtil) {
        this.authorityRepo = authorityRepo;
        this.dtoUtil = dtoUtil;
    }

    @PreAuthorize("hasAuthority('AUTHORITY')")
    @Transactional(readOnly = true)
    @Override
    public PaginationRes<AuthorityRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting paginated Authority. -----");

        List<String> fields = Arrays.asList("title");
        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return new PaginationDtoUtil<AuthorityEntity, AuthorityReq, AuthorityRes>().paginate(paginationReq, fields, sortBy, sortOrder, authorityRepo, dtoUtil);
    }
}
