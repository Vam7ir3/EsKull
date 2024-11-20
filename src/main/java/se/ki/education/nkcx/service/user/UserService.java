package se.ki.education.nkcx.service.user;

import se.ki.education.nkcx.dto.request.PaginationReq;
import se.ki.education.nkcx.dto.response.PaginationRes;
import se.ki.education.nkcx.dto.request.PasswordReq;
import se.ki.education.nkcx.dto.request.UserReq;
import se.ki.education.nkcx.dto.response.UserResDto;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.repo.projection.UserProjection;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserResDto save(UserReq userReq) throws IOException;

    PaginationRes<UserResDto> get(PaginationReq paginationReq);

    PaginationRes<UserResDto> getByRole(PaginationReq paginationReq, Long roleId);

    PaginationRes<UserProjection> getByRoleTitles(PaginationReq paginationReq, List<String> roleTitles);

    UserResDto get();

    PaginationRes<UserProjection> getLimited(List<String> fields, PaginationReq paginationReq);

    UserResDto update(UserReq userReq) throws IOException;

    boolean changePassword(PasswordReq passwordReq);

    void delete(Long userId);

    void logout();

    void logActivity(String operation, String description, String entityName,  UserEntity user);
    UserEntity getCurrentUser();
}
