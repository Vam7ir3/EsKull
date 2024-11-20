package se.ki.education.nkcx.config.jwt;

import se.ki.education.nkcx.config.AuthenticatedUser;
import se.ki.education.nkcx.enums.LanguageCode;
import se.ki.education.nkcx.util.CommonValue;
import se.ki.education.nkcx.util.ConfigUtil;
import se.ki.education.nkcx.util.PropertiesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private final Logger LOG = LogManager.getLogger();

    @Qualifier("customUserDetailsService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private CommonValue commonValue;

    @Autowired
    private PropertiesUtil propertiesUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authToken = request.getHeader(ConfigUtil.INSTANCE.getTokenHeader());

        String clientLanguage = request.getHeader("language");
        if (clientLanguage != null && !clientLanguage.isEmpty() && LanguageCode.exists(clientLanguage)) {
            commonValue.setClientLanguageCode(LanguageCode.valueOf(clientLanguage));
        }

        if (authToken != null && !authToken.isEmpty()) {
            if (!authToken.startsWith("Bearer ")) {
                throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, propertiesUtil.getMessage("ERR004"));
            }
            authToken = authToken.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // It is not compelling necessary to load the user details from the database. You could also store the information
                // in the token and read it from it. It's up to you ;)
                AuthenticatedUser authenticatedUser = (AuthenticatedUser) this.userDetailsService.loadUserByUsername(username);

                // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                // the database compellingly. Again it's up to you ;)
                if (jwtTokenUtil.validateToken(authToken, authenticatedUser)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
                    WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                    Map<String, Object> map = new HashMap<>();
                    map.put("webAuthenticationDetails", webAuthenticationDetails);
                    authentication.setDetails(map);
                    //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    LOG.info("----- Authenticated User: " + username + ", setting security context. -----");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }
}