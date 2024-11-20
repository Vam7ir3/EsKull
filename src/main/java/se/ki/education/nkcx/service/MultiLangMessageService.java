package se.ki.education.nkcx.service;

import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.MultiLangMessageReq;
import se.ki.education.nkcx.dto.response.MultiLangMessageRes;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.dto.util.MultiLangMessageDtoUtil;
import se.ki.education.nkcx.entity.MultiLangMessageEntity;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.MultiLangMessageRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@PreAuthorize("hasAuthority('MULTI_LANG_MESSAGE')")
@Service
@Transactional(rollbackFor = Exception.class)
public class MultiLangMessageService {

    private static final Logger LOG = LogManager.getLogger();

    private final MultiLangMessageRepo multiLangMessageRepo;
    private final DtoUtil<MultiLangMessageEntity, MultiLangMessageReq, MultiLangMessageRes> dtoUtil;
    private final PaginationDtoUtil<MultiLangMessageEntity, MultiLangMessageReq, MultiLangMessageRes> paginationDtoUtil;

    @Autowired
    public MultiLangMessageService(MultiLangMessageRepo multiLangMessageRepo, MultiLangMessageDtoUtil multiLangMessageDtoUtil, PaginationDtoUtil<MultiLangMessageEntity, MultiLangMessageReq, MultiLangMessageRes> paginationDtoUtil) {
        this.multiLangMessageRepo = multiLangMessageRepo;
        this.dtoUtil = multiLangMessageDtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    public PaginationRes<MultiLangMessageRes> get(PaginationReq paginationReq) {
        LOG.info("----- Getting MultiLangMessage. -----");

        List<String> fields = Arrays.asList("code", "english", "swedish", "spanish");
        String sortBy = "code";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, multiLangMessageRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('MULTI_LANG_MESSAGE_U')")
    public MultiLangMessageRes update(MultiLangMessageReq multiLangMessageReq) {
        LOG.info("----- Updating MultiLangMessage. -----");

        Optional<MultiLangMessageEntity> MultiLangMessageEntity = multiLangMessageRepo.findById(multiLangMessageReq.getId());
        MultiLangMessageEntity.orElseThrow(() -> new CustomException("MLM001"));

        dtoUtil.setUpdatedValue(multiLangMessageReq, MultiLangMessageEntity.get());

        return dtoUtil.prepRes(MultiLangMessageEntity.get());
    }
}
