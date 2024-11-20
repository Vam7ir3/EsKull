package se.ki.education.nkcx.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/* Created by: Binay Singh */

@Service
public class ConfigUtil {
    public static ConfigUtil INSTANCE;

    @Value("${nkcx.server}")
    private String runningEnvironment;

    @Value("${file.saving.directory}")
    private String fileSavingDirectory;

    @Value("${file.saving.directory.temp}")
    private String fileSavingDirectoryTemp;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.expiration}")
    private Long expiration;

    @PostConstruct
    public void init() {
        INSTANCE = this;
    }

    public String getRunningEnvironment() {
        return runningEnvironment;
    }

    public String getFileSavingDirectory() {
        return fileSavingDirectory;
    }

    public String getFileSavingDirectoryTemp() {
        return fileSavingDirectoryTemp;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }

    public Long getExpiration() {
        return expiration;
    }

}
