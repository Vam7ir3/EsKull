package se.ki.education.nkcx.util;

import se.ki.education.nkcx.enums.LanguageCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class PropertiesUtil {

    private static final Logger LOG = LogManager.getLogger();

    private static final String ERROR_CODE_FILE_EN = "error-code-en.properties";
    private static final String ERROR_CODE_FILE_SV = "error-code-sv.properties";

    private final CommonValue commonValue;

    public PropertiesUtil(CommonValue commonValue) {
        this.commonValue = commonValue;
    }

    public String getMessage(String key) {
        return getPropertyValue(key);
    }

    private String getPropertyValue(String key) {
        try {
            Properties properties;
            if (commonValue.getClientLanguageCode().equals(LanguageCode.SWEDISH)) {
                properties = readFileAsProperties(ERROR_CODE_FILE_SV);
            } else {
                properties = readFileAsProperties(ERROR_CODE_FILE_EN);
            }
            return properties.getProperty(key);
        } catch (IOException ioe) {
            LOG.error("----- Error occurred while reading the file. -----");
            return null;
        }
    }

    private Properties readFileAsProperties(String fileName) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
