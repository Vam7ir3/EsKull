package se.ki.education.nkcx.config.jwt;

import se.ki.education.nkcx.config.AuthenticatedUser;
import se.ki.education.nkcx.config.CustomUserDetailsService;
import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.dto.request.LoginReq;
import se.ki.education.nkcx.dto.request.UserReq;
import se.ki.education.nkcx.dto.response.UserResDto;
import se.ki.education.nkcx.dto.util.DtoUtil;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.exception.CustomException;
import se.ki.education.nkcx.repo.UserRepo;
import se.ki.education.nkcx.service.general.LogService;
import se.ki.education.nkcx.util.ConfigUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.ZoneId;

@RestController
@RequestMapping(value = "/api")
@Api(tags = "Authentication")
public class AuthenticationRestController {

    @Autowired
    private final LogService logService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private DtoUtil<UserEntity, UserReq, UserResDto> dtoUtil;

    public AuthenticationRestController(LogService logService) {
        this.logService = logService;
    }

    @Transactional
    @PostMapping(value = "/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginReq authenticationDetail) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDetail.getUsername(),
                        authenticationDetail.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final AuthenticatedUser authenticatedUser = userDetailsService.loadUserByUsername(authenticationDetail.getUsername());
        String token = jwtTokenUtil.generateToken(authenticatedUser);

        UserEntity userEntity = userRepo.findById(authenticatedUser.getId()).get();

        logService.logActivity(
                "Login", "User Logged in",  userEntity
        );
        // Return the token
        return ResponseEntity.ok(
                new ServiceRes()
                        .setStatus(true)
                        .setMessage("Token generated.")
                        .addData("token", token)
                        .addData("expireIn", ConfigUtil.INSTANCE.getExpiration())
                        .addData("user", dtoUtil.prepRes(userEntity))
        );
    }

    @GetMapping(value = "/auth/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(ConfigUtil.INSTANCE.getTokenHeader());

        if (!token.startsWith("Bearer ")) {
            throw new CustomException("ERR004");
        }
        token = token.substring(7);

        String username = jwtTokenUtil.getUsernameFromToken(token);
        AuthenticatedUser authenticatedUser = userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, Date.from(authenticatedUser.getLastPasswordResetDate().atZone(ZoneId.systemDefault()).toInstant()))) {

            String refreshedToken = jwtTokenUtil.refreshToken(token);

            return ResponseEntity.ok(
                    new ServiceRes()
                            .setStatus(true)
                            .setMessage("Token refreshed.")
                            .addData("token", refreshedToken)
                            .addData("expireIn", ConfigUtil.INSTANCE.getExpiration())
                            .addData("user", dtoUtil.prepRes(userRepo.findById(authenticatedUser.getId()).get()))
            );

        }
        return ResponseEntity.badRequest().body(new ServiceRes().setStatus(false).setMessage("Failed to refresh token."));
    }

//    @GetMapping(value = "/logs")
//    public ResponseEntity<?> getAllLogs() {
//        List<LogRes> logs = logService.getAllLogs();
//        return ResponseEntity.ok(
//                new ServiceRes()
//                        .setStatus(true)
//                        .setMessage("Logs retrieved successfully.")
//                        .addData("logs", logs)
//        );
//    }

}
