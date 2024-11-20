package se.ki.education.nkcx.exception;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

public class MyErrorAttributes implements ErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status", includeStackTrace);
        errors.put("message", "You don't have permission to access this resource.");
        return errors;
    }

    @Override
    public Throwable getError(WebRequest webRequest) {
        return null;
    }
}
