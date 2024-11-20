package se.ki.education.nkcx.service.user;

import se.ki.education.nkcx.config.SessionManager;
import se.ki.education.nkcx.dto.util.PaginationDtoUtil;
import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.PasswordReq;
import se.ki.education.nkcx.dto.request.UserReq;
import se.ki.education.nkcx.dto.response.UserResDto;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.*;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.*;
import se.ki.education.nkcx.repo.projection.UserProjection;
import se.ki.education.nkcx.repo.specification.SearchCriteria;
import se.ki.education.nkcx.repo.specification.UserSpecificationBuilder;
import se.ki.education.nkcx.util.FileUtil;
import se.ki.education.nkcx.util.GeneralUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger();
    private static final String USER_IMAGE_DIRECTORY = "user_image";

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final LogRepo logRepo;
    private final VerificationRepo verificationRepo;
    private final DtoUtil<UserEntity, UserReq, UserResDto> dtoUtil;
    private final PaginationDtoUtil<UserEntity, UserReq, UserResDto> paginationDtoUtil;
    private final AllowedRegistrationRepo allowedRegistrationRepo;

    private final List<String> fields = Arrays.asList("firstName", "lastName", "gender", "dateOfBirth", "mobileNumber", "emailAddress");
    private String sortBy = "firstName";//Default sortBy
    private Sort.Direction sortOrder = Sort.Direction.ASC;//Default sortOrder


    @Autowired
    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, LogRepo logRepo, VerificationRepo verificationRepo, DtoUtil<UserEntity, UserReq, UserResDto> dtoUtil, PaginationDtoUtil<UserEntity, UserReq, UserResDto> paginationDtoUtil, AllowedRegistrationRepo allowedRegistrationRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.logRepo = logRepo;
        this.verificationRepo = verificationRepo;
        this.paginationDtoUtil = paginationDtoUtil;
        this.dtoUtil = dtoUtil;
        this.allowedRegistrationRepo = allowedRegistrationRepo;
    }

    @Override
    public UserResDto save(UserReq userReq) throws IOException {
        LOG.info("----- Saving User. -----");
        UserEntity user = dtoUtil.reqToEntity(userReq).setLastPasswordResetDate(LocalDateTime.now());
        user.setPassword(new BCryptPasswordEncoder().encode(userReq.getPassword()));

        VerificationEntity verificationEntity = null;

        if (!SessionManager.isSuperAdmin()) {
            if (userReq.getVerification() == null || userReq.getVerification().getId() == null
                    || userReq.getVerification().getVerificationCode() == null) {
                throw new CustomException("VER001");
            }

            verificationEntity = verificationRepo.findByIdAndVerificationCodeAndDeadlineAfter(
                    userReq.getVerification().getId(),
                    GeneralUtil.sha256(userReq.getVerification().getVerificationCode()),
                    LocalDateTime.now(ZoneOffset.UTC)
            );
            if (verificationEntity == null) {
                throw new CustomException("VER002");
            }

            if (userReq.getVerification().getEmailAddress() != null
                    && !userReq.getEmailAddress().equals(userReq.getVerification().getEmailAddress())) {
                throw new CustomException("VER003");
            }
            if (userReq.getVerification().getMobileNumber() != null
                    && !userReq.getMobileNumber().equals(userReq.getVerification().getMobileNumber())) {
                throw new CustomException("VER004");
            }

            if (verificationEntity.getEmailAddress() != null) {

                Optional<AllowedRegistrationEntity> optionalAllowedRegistrationEntity = allowedRegistrationRepo.findByEmail(userReq.getEmailAddress());
                if (!optionalAllowedRegistrationEntity.isPresent()) {
                    throw new CustomException("VER006");
                }
                user.setEmailAddressVerified(true);
                optionalAllowedRegistrationEntity.get().setRegisteredDateTime(LocalDateTime.now(ZoneOffset.UTC));
            }
            if (verificationEntity.getMobileNumber() != null) {
                user.setMobileNumberVerified(true);
            }

            Optional<RoleEntity> optionalRoleEntity = roleRepo.findByTitle("Participant");
            optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));

            user.setRole(optionalRoleEntity.get());

        } else {
            if (userReq.getEmailAddress() != null) {
                Optional<AllowedRegistrationEntity> optionalAllowedRegistrationEntity = allowedRegistrationRepo.findByEmail(userReq.getEmailAddress());
                if (!optionalAllowedRegistrationEntity.isPresent()) {
                    throw new CustomException("VER006");
                }
                user.setEmailAddressVerified(true);
                optionalAllowedRegistrationEntity.get().setRegisteredDateTime(LocalDateTime.now(ZoneOffset.UTC));
            }
            if (userReq.getMobileNumber() != null) {
                user.setMobileNumberVerified(true);
            }

            RoleEntity roleEntity;
            if (userReq.getRoleId() != null) {
                Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(userReq.getRoleId());
                optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
                roleEntity = optionalRoleEntity.get();
            } else {
                Optional<RoleEntity> optionalRoleEntity = roleRepo.findByTitle("Participant");
                optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
                roleEntity = optionalRoleEntity.get();
            }
            user.setRole(roleEntity);
        }

        if (userReq.getEmailAddress() == null || userReq.getMobileNumber() == null) {
            throw new CustomException("VER005");
        }

        if (userRepo.findByEmailAddress(userReq.getEmailAddress()) != null) {
            throw new CustomException("USR005");
        }
        if (userRepo.findByMobileNumber(userReq.getMobileNumber()) != null) {
            throw new CustomException("USR006");
        }

        UserEntity userEntity = userRepo.save(user);

        if (userReq.getImage() != null) {
            String imageUrl = FileUtil.saveFile(userReq.getImage(), USER_IMAGE_DIRECTORY, userEntity.getId().toString());
            userEntity.setImageUrl(imageUrl);
        }

        if (verificationEntity != null) {
            verificationRepo.delete(verificationEntity);
        }

        logActivity("SAVE_USER", "Saved user with ID: " + userEntity.getId(), "UserEntity", userEntity);


        return dtoUtil.prepRes(userEntity);
    }

    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationRes<UserResDto> get(PaginationReq paginationReq) {
        LOG.info("----- Getting paginated Users. -----");

        List<String> validFields = Arrays.asList("firstName", "lastName", "gender", "dateOfBirth", "mobileNumber", "emailAddress");

        String sortBy = "emailAddress";
        Sort.Direction sortOrder = Sort.Direction.ASC;

        if (paginationReq.getSortBy() != null) {
            if (validFields.contains(paginationReq.getSortBy())) {
                sortBy = paginationReq.getSortBy();
            }
        }

        if (paginationReq.getSortOrder() != null &&
                paginationReq.getSortOrder().equalsIgnoreCase("DESC")) {
            sortOrder = Sort.Direction.DESC;
        }

        List<SearchCriteria> searchCriteriaList = new ArrayList<>();
        if (paginationReq.getSearchTerm() != null) {
            searchCriteriaList.add(new SearchCriteria("firstName", ":", paginationReq.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("lastName", ":", paginationReq.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("mobileNumber", ":", paginationReq.getSearchTerm(), true));
            searchCriteriaList.add(new SearchCriteria("emailAddress", ":", paginationReq.getSearchTerm(), true));
        }

        Page<UserEntity> pageUserEntity = null;
        List<UserEntity> userEntities;

        if (paginationReq.getPageSize() > 0) {
            pageUserEntity = userRepo.findAll(
                    new UserSpecificationBuilder().with(searchCriteriaList).build(),
                    PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), sortOrder, sortBy)
            );
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAll(
                    new UserSpecificationBuilder().with(searchCriteriaList).build(),
                    Sort.by(sortOrder, sortBy)
            );
        }

        List<UserResDto> userResDtos = userEntities.stream()
                .map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(pageUserEntity, userResDtos);
    }

    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationRes<UserResDto> getByRole(PaginationReq paginationReq, Long roleId) {
        LOG.info("----- Getting paginated User by Role. -----");

        Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(roleId);
        optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));

        if (paginationReq.getSortBy() != null && fields.contains(paginationReq.getSortBy())
                && paginationReq.getSortOrder() != null) {
            sortBy = paginationReq.getSortBy();
            sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<UserEntity> pageUserEntity = null;
        List<UserEntity> userEntities;
        if (paginationReq.getPageSize() > 0) {
            pageUserEntity = userRepo.findAllByRole(optionalRoleEntity.get(), PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), sortOrder, sortBy));
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAllByRole(optionalRoleEntity.get(), Sort.by(sortOrder, sortBy));
        }

        List<UserResDto> userResDtos = userEntities.stream().map(dtoUtil::prepRes)
                .collect(Collectors.toList());

        return paginationDtoUtil.prepPaginationDto(pageUserEntity, userResDtos);
    }


    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationRes<UserProjection> getByRoleTitles(PaginationReq paginationReq, List<String> roleTitles) {
        LOG.info("----- Getting paginated User by Role Titles. -----");

        if (paginationReq.getSortBy() != null && fields.contains(paginationReq.getSortBy())
                && paginationReq.getSortOrder() != null) {
            sortBy = paginationReq.getSortBy();
            sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<UserProjection> pageUserEntity = null;
        List<UserProjection> userEntities;
        if (paginationReq.getPageSize() > 0) {
            pageUserEntity = userRepo.findAllByRole_TitleIn(roleTitles, PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), sortOrder, sortBy));
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAllByRole_TitleIn(roleTitles, Sort.by(sortOrder, sortBy));
        }

        PaginationRes<UserProjection> paginationRes = new PaginationRes<>();

        if (pageUserEntity != null) {
            paginationRes
                    .setStartPosition(pageUserEntity.getNumber() * pageUserEntity.getSize() + 1)
                    .setEndPosition((pageUserEntity.getNumber() * pageUserEntity.getSize() + 1) + (pageUserEntity.getContent().size() - 1))
                    .setTotalRecord(pageUserEntity.getTotalElements())
                    .setTotalPage(pageUserEntity.getTotalPages())
                    .setPageSize(pageUserEntity.getSize())
                    .setCurrentPage(pageUserEntity.getNumber() + 1);
        }
        return paginationRes.setData(userEntities);
    }


    @PreAuthorize("hasAuthority('USER_RA')")
    @Transactional(readOnly = true)
    @Override
    public PaginationRes<UserProjection> getLimited(List<String> fields, PaginationReq paginationReq) {
        LOG.info("----- Getting paginated User by Role Titles. -----");

        if (paginationReq.getSortBy() != null && fields.contains(paginationReq.getSortBy())
                && paginationReq.getSortOrder() != null) {
            sortBy = paginationReq.getSortBy();
            sortOrder = paginationReq.getSortOrder().equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        }

        Page<UserProjection> pageUserEntity = null;
        List<UserProjection> userEntities;
        if (paginationReq.getPageSize() > 0) {
            pageUserEntity = userRepo.findAllByEmailAddressNotNull(PageRequest.of(paginationReq.getPageNumber(), paginationReq.getPageSize(), sortOrder, sortBy));
            userEntities = pageUserEntity.getContent();
        } else {
            userEntities = userRepo.findAllByEmailAddressNotNull(Sort.by(sortOrder, sortBy));
        }

        PaginationRes<UserProjection> paginationRes = new PaginationRes<>();

        if (pageUserEntity != null) {
            paginationRes
                    .setStartPosition(pageUserEntity.getNumber() * pageUserEntity.getSize() + 1)
                    .setEndPosition((pageUserEntity.getNumber() * pageUserEntity.getSize() + 1) + (pageUserEntity.getContent().size() - 1))
                    .setTotalRecord(pageUserEntity.getTotalElements())
                    .setTotalPage(pageUserEntity.getTotalPages())
                    .setPageSize(pageUserEntity.getSize())
                    .setCurrentPage(pageUserEntity.getNumber() + 1);
        }
        return paginationRes.setData(userEntities);
    }

    @PreAuthorize("hasAuthority('USER')")
    @Transactional(readOnly = true)
    @Override
    public UserResDto get() {
        LOG.info("----- Getting User. -----");
        Optional<UserEntity> optionalUserEntity = userRepo.findById(SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> null);

        return dtoUtil.prepRes(optionalUserEntity.get());
    }

    @PreAuthorize("hasAuthority('USER_U')")
    @Override
    public UserResDto update(UserReq userReq) throws IOException {
        LOG.info("----- Updating User. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(userReq.getId() != null ? userReq.getId() : SessionManager.getUserId());
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));

        UserEntity userEntity = optionalUserEntity.get();

        if (SessionManager.isSuperAdmin() && userReq.getRoleId() != null) {
            Optional<RoleEntity> optionalRoleEntity = roleRepo.findById(userReq.getRoleId());
            optionalRoleEntity.orElseThrow(() -> new CustomException("ROL001"));
            userEntity.setRole(optionalRoleEntity.get());
        }

        if (userReq.getEmailAddress() != null && !userReq.getEmailAddress().equalsIgnoreCase(userEntity.getEmailAddress())
                && userRepo.findByEmailAddress(userReq.getEmailAddress()) != null) {
            throw new CustomException("USR002");
        }

        if (userReq.getMobileNumber() != null && !userReq.getMobileNumber().equalsIgnoreCase(userEntity.getMobileNumber())
                && userRepo.findByMobileNumber(userReq.getMobileNumber()) != null) {
            throw new CustomException("USR002");
        }

        if (userReq.getImage() != null && userReq.getImage().startsWith("data:image/")) {
            if (userEntity.getImageUrl() != null) {
                FileUtil.deleteFile(FileUtil.getAbsolutePathFromFileUrl(userEntity.getImageUrl()));
            }
            String imageUrl = FileUtil.saveFile(userReq.getImage(), USER_IMAGE_DIRECTORY, userEntity.getId().toString() + "_" + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli());
            userEntity.setImageUrl(imageUrl);
        }

        dtoUtil.setUpdatedValue(userReq, userEntity);
        logActivity("UPDATE_USER", "Updated user with ID: " + userReq.getId(), "UserEntity", userEntity);
        return dtoUtil.prepRes(userEntity);
    }

    @PreAuthorize("hasAuthority('CHANGE_PASSWORD')")
    @Override
    public boolean changePassword(PasswordReq passwordReq) {
        LOG.info("----- Changing password. -----");

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserEntity userEntity;
        if (passwordReq.getUserId() != null) {
            Optional<UserEntity> optionalUserEntity = userRepo.findById(passwordReq.getUserId());
            optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
            userEntity = optionalUserEntity.get();
        } else {
            userEntity = userRepo.findByEmailAddress(passwordReq.getEmailAddress());
        }

        if (userEntity == null) {
            throw new CustomException("USR001");
        }
        if (!userEntity.getId().equals(SessionManager.getUserId())) {
            throw new CustomException("USR003");
        }
        if (!GeneralUtil.matchPassword(passwordReq.getOldPassword(), userEntity.getPassword())) {
            throw new CustomException("ERR002");
        }

        userEntity.setPassword(bCryptPasswordEncoder.encode(passwordReq.getNewPassword()));
        userEntity.setLastPasswordResetDate(LocalDateTime.now().atOffset(ZoneOffset.UTC).toLocalDateTime());
        return true;
    }

    @PreAuthorize("hasAuthority('USER_D')")
    @Override
    public void delete(Long userId) {
        LOG.info("----- Deleting User permanently. -----");

        Optional<UserEntity> optionalUserEntity = userRepo.findById(userId);
        optionalUserEntity.orElseThrow(() -> new CustomException("USR001"));
        if (optionalUserEntity.get().getRole().getTitle().equalsIgnoreCase("Super Admin")) {
            throw new CustomException("ERR005");
        }

        userRepo.delete(optionalUserEntity.get());

        logActivity("DELETE_USER", "Deleted user with ID: " + userId, "UserEntity", optionalUserEntity.get());


    }

    @Override
    public void logout() {
        LOG.info("----- Logging out user by marking loggedout. -----");
    }

    @Override
    public void logActivity(String operation, String description, String entityName, UserEntity user) {
        if (description == null || operation == null || entityName == null || user == null) {
            throw new CustomException("ERR006");
        }

        LOG.info("----- Logging user activity -----");

        LogEntity logEntity = new LogEntity();

        logEntity.setOperation(operation);
        logEntity.setDescription(description);
        logEntity.setUserEntity(user);
        logEntity.setTimestamp(LocalDateTime.now());

        logRepo.save(logEntity);

    }


    @Override
    public UserEntity getCurrentUser() {
        return null;
    }
}
