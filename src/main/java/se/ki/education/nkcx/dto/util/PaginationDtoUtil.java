package se.ki.education.nkcx.dto.util;

import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.entity.CommonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaginationDtoUtil<Entity extends CommonEntity, Req, Res> {

    public PaginationRes<Res> prepPaginationDto(Page<Entity> page, List<Res> data) {
        PaginationRes<Res> paginationRes = new PaginationRes<>();

        if (page != null) {
            paginationRes
                    .setStartPosition(page.getNumber() * page.getSize() + 1)
                    .setEndPosition((page.getNumber() * page.getSize() + 1) + (page.getContent().size() - 1))
                    .setTotalRecord(page.getTotalElements())
                    .setTotalPage(page.getTotalPages())
                    .setPageSize(page.getSize())
                    .setCurrentPage(page.getNumber() + 1);
        }
        return paginationRes.setData(data);
    }

    public PaginationRes<Res> paginate(PaginationReq paginationReq, List<String> fields, String sortBy, Sort.Direction sortOrder, JpaRepository<Entity, Long> repository, DtoUtil<Entity, Req, Res> dtoUtil) {
        if (paginationReq.getSortBy() != null && fields.contains(paginationReq.getSortBy())
                && paginationReq.getSortOrder() != null) {
            sortBy = paginationReq.getSortBy();
            sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<Entity> entityPage = null;
        List<Entity> entities;
        if (paginationReq.getPageSize() > 0) {
            entityPage = repository.findAll(PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), sortOrder, sortBy));
            entities = entityPage.getContent();
        } else {
            entities = repository.findAll(Sort.by(sortOrder, sortBy));
        }

        List<Res> resDtos = entities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return prepPaginationDto(entityPage, resDtos);
    }
}
