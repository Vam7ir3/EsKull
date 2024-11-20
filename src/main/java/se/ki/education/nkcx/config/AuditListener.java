package se.ki.education.nkcx.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import se.ki.education.nkcx.entity.LogEntity;
import se.ki.education.nkcx.entity.UserEntity;
import se.ki.education.nkcx.repo.LogRepo;
import se.ki.education.nkcx.repo.UserRepo;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AuditListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditListener.class);

    private final LogRepo logRepo;
    private final UserRepo userRepo;

    @Autowired
    public AuditListener(LogRepo logRepo, UserRepo userRepo) {
        this.logRepo = logRepo;
        this.userRepo = userRepo;
    }



    @PostPersist
    public void beforeCreate(Object entity){
        if (entity instanceof LogEntity) {
            LogEntity logEntity = (LogEntity) entity;
            if(logEntity.getUserEntity()==null){
                throw new IllegalStateException("User is null");
            }
        }
    }

//    @PrePersist
//    private void beforeCreate(Object entity){
//        createLogEntry("CREATE", entity);
//    }

    @PreRemove
    private void beforeRemove(Object entity){
        createLogEntry("DELETE", entity);
    }

    private void createLogEntry(String operation, Object entity){
        if (entity instanceof LogEntity){
            return;
        }
        LogEntity logEntity = new LogEntity();
        logEntity.setDescription(operation + " operation performed on " + entity.getClass().getSimpleName());
        logEntity.setOperation(operation);
        logEntity.setCreatedDate(LocalDateTime.now());

        UserEntity currentUser = getCurrentUser();
        logEntity.setUserEntity(currentUser);

        logRepo.save(logEntity);
        logger.info("Log entry created: {}", logEntity);
    }

    private UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser) {
            AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
            return userRepo.findById(authenticatedUser.getId()).orElse(null);
        }
        logger.warn("User is not authenticated");
        return null;
    }

}
