package se.ki.education.nkcx.config;

import se.ki.education.nkcx.entity.RoleEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.ki.education.nkcx.service.general.LogService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    private final LogService logService;

    @Autowired
    public CustomUserDetailsService(UserRepo userRepo, LogService logService) {
        this.userRepo = userRepo;
        this.logService = logService;
    }

    @Override
    public AuthenticatedUser loadUserByUsername(String username) {
        boolean isMobileNumber = false;
        UserEntity userEntity = userRepo.findByEmailAddressAndEmailAddressVerifiedTrue(username);
        if (userEntity == null) {
            userEntity = userRepo.findByMobileNumberAndMobileNumberVerifiedTrue(username);
            if (userEntity == null) {
                throw new UsernameNotFoundException("User not found.");
            } else {
                isMobileNumber = true;
            }
        }
//        logLoginAttempt(userEntity);

        return buildUserForAuthentication(userEntity, isMobileNumber);
    }

//    private void logLoginAttempt(UserEntity userEntity) {
//        logService.logActivity(
//                "LOGIN",
//                "User logged in",
//                "UserEntity",
//                userEntity
//        );
//    }


    private AuthenticatedUser buildUserForAuthentication(UserEntity user, boolean isMobileNumber) {
        String username;
        if (isMobileNumber) {
            username = user.getMobileNumber();
        } else {
            username = user.getEmailAddress();
        }
        return new AuthenticatedUser(username, user.getPassword(), true,
                true, true, true, buildGrantedAuthorityForUser(user.getRole()),
                user.getId(), user.getRole().getTitle(), user.getLastPasswordResetDate());
    }

    private Collection<? extends GrantedAuthority> buildGrantedAuthorityForUser(RoleEntity role) {
        return role.getAuthorities().stream()
                .map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.getTitle()))
                .collect(Collectors.toList());
    }
}
