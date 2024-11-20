package se.ki.education.nkcx.service.general;

import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.RoleReq;
import se.ki.education.nkcx.dto.response.RoleResDto;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.AuthorityEntity;
import se.ki.education.nkcx.entity.RoleEntity;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.AuthorityRepo;
import se.ki.education.nkcx.repo.RoleRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class RoleServiceImpl implements RoleService {

    private static final Logger LOG = LogManager.getLogger();

    private final RoleRepo roleRepo;
    private final AuthorityRepo authorityRepo;
    private final DtoUtil<RoleEntity, RoleReq, RoleResDto> dtoUtil;
    private final PaginationDtoUtil<RoleEntity, RoleReq, RoleResDto> paginationDtoUtil;

    @Autowired
    public RoleServiceImpl(RoleRepo roleRepo, AuthorityRepo authorityRepo, DtoUtil<RoleEntity, RoleReq, RoleResDto> dtoUtil, PaginationDtoUtil<RoleEntity, RoleReq, RoleResDto> paginationDtoUtil) {
        this.roleRepo = roleRepo;
        this.authorityRepo = authorityRepo;
        this.dtoUtil = dtoUtil;
        this.paginationDtoUtil = paginationDtoUtil;
    }

    @PreAuthorize("hasAuthority('ROLE_C')")
    @Override
    public RoleResDto save(RoleReq roleReq) {
        LOG.info("----- Saving Role. -----");
        if (roleRepo.findByTitle(roleReq.getTitle()).isPresent()) {
            throw new CustomException("ROL002");
        }

        RoleEntity role = dtoUtil.reqToEntity(roleReq);
        if (roleReq.getAuthorityIds() != null && roleReq.getAuthorityIds().size() > 0) {
            role.setAuthorities(
                    roleReq.getAuthorityIds().stream().map(id -> {
                        Optional<AuthorityEntity> optionalAuthorityEntity = authorityRepo.findById(id);
                        optionalAuthorityEntity.orElseThrow(() -> new CustomException("AUT002"));
                        return optionalAuthorityEntity.get();
                    }).collect(Collectors.toList())
            );
        }
        return dtoUtil.prepRes(roleRepo.save(role));
    }

    @PreAuthorize("hasAuthority('ROLE_RA')")
    @Override
    public PaginationRes<RoleResDto> get(PaginationReq paginationReq) {
        LOG.info("----- Getting paginated Role. -----");

        List<String> fields = Arrays.asList("title");
        String sortBy = "title";//Default sortBy
        Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder
        return paginationDtoUtil.paginate(paginationReq, fields, sortBy, sortOrder, roleRepo, dtoUtil);
    }

    @PreAuthorize("hasAuthority('ROLE_U')")
    @Override
    public RoleResDto update(RoleReq roleReq) {
        LOG.info("----- Updating Role. -----");

        Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(roleReq.getId());
        optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
        RoleEntity roleEntity = optionalRoleEntity.get();

        if (roleReq.getTitle() != null && !roleReq.getTitle().equalsIgnoreCase(roleEntity.getTitle())
                && roleRepo.findByTitle(roleReq.getTitle()).isPresent()) {
            throw new CustomException("ROL002");
        }
        if (roleReq.getAuthorityIds() != null) {

            //Add authorityIds
            List<AuthorityEntity> newAuthorities = new ArrayList<>();
            for (Long authorityId : roleReq.getAuthorityIds()) {
                Optional<AuthorityEntity> optionalAuthorityEntity = authorityRepo.findById(authorityId);
                optionalAuthorityEntity.orElseThrow(() -> new CustomException("AUT002"));
                AuthorityEntity authorityEntity = optionalAuthorityEntity.get();

                newAuthorities.add(authorityEntity);
                if (!roleEntity.getAuthorities().contains(authorityEntity)) {
                    roleEntity.getAuthorities().add(authorityEntity);
                }
            }

            //Remove authorityIds
            List<AuthorityEntity> authoritiesToRemove = new ArrayList<>();
            for (AuthorityEntity authorityEntity : roleEntity.getAuthorities()) {
                if (!newAuthorities.contains(authorityEntity)) {
                    authoritiesToRemove.add(authorityEntity);
                }
            }
            roleEntity.getAuthorities().removeAll(authoritiesToRemove);
        }
        dtoUtil.setUpdatedValue(roleReq, roleEntity);
        return dtoUtil.prepRes(roleEntity);
    }

    @PreAuthorize("hasAuthority('ROLE_D')")
    @Override
    public void delete(Long id) {
        LOG.info("----- Deleting Role. -----");

        Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(id);
        optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));

        roleRepo.delete(optionalRoleEntity.get());
    }
}
