package se.ki.education.nkcx.config.jwt;

import se.ki.education.nkcx.dto.response.ServiceRes;
import se.ki.education.nkcx.enums.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
        PrintWriter printWriter = response.getWriter();
        ServiceRes serviceRes = new ServiceRes().setStatus(false).setMessage(ErrorMessage.ERR003.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        printWriter.write(objectMapper.writeValueAsString(serviceRes));
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}