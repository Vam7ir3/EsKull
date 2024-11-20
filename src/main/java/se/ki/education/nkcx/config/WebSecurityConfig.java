package se.ki.education.nkcx.config;

import se.ki.education.nkcx.config.jwt.JwtAuthenticationEntryPoint;
import se.ki.education.nkcx.config.jwt.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().and()
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // allow anonymous resource requests
                .antMatchers(HttpMethod.GET,"/home.html").permitAll()
                .antMatchers(HttpMethod.GET, "/resources/**").permitAll()
                .antMatchers(HttpMethod.GET, "/").permitAll()
                .antMatchers(HttpMethod.GET, "/dashboard.html").permitAll()
                .antMatchers(HttpMethod.GET, "/my_upload.html").permitAll()
                .antMatchers(HttpMethod.GET, "/upload_shared.html").permitAll()
                .antMatchers(HttpMethod.GET, "/sample_shared.html").permitAll()
                .antMatchers(HttpMethod.GET, "/sample_upload.html").permitAll()
                .antMatchers(HttpMethod.GET, "/user_shared.html").permitAll()
                .antMatchers(HttpMethod.GET, "/user.html", "/user_profile.html").permitAll()
                .antMatchers(HttpMethod.GET, "/role.html").permitAll()
                .antMatchers(HttpMethod.GET, "/user").permitAll()
                .antMatchers(HttpMethod.GET, "/lab").permitAll()
                .antMatchers(HttpMethod.GET, "/cell6923").permitAll()
                .antMatchers(HttpMethod.GET, "/cell6923.html").permitAll()
                .antMatchers(HttpMethod.GET, "/district").permitAll()
                .antMatchers(HttpMethod.GET, "/klartext").permitAll()
                .antMatchers(HttpMethod.GET, "/klartext.html").permitAll()
                .antMatchers(HttpMethod.GET, "/invitationType").permitAll()
                .antMatchers(HttpMethod.GET, "/invitationType.html").permitAll()
                .antMatchers(HttpMethod.GET, "/parish").permitAll()
                .antMatchers(HttpMethod.GET, "/parish.html").permitAll()
                .antMatchers(HttpMethod.GET, "/municipality").permitAll()
                .antMatchers(HttpMethod.GET, "/lab.html").permitAll()
                .antMatchers(HttpMethod.GET, "/authority.html").permitAll()
                .antMatchers(HttpMethod.GET, "/lab_analysis_technical_platform.html","/lab_analysis_output.html",
                        "/lab_analysis_format.html").permitAll()
                .antMatchers(HttpMethod.GET, "/link.html").permitAll()
                .antMatchers(HttpMethod.GET,"/person.html").permitAll()
                .antMatchers(HttpMethod.GET, "/allowed_registration.html").permitAll()
                .antMatchers(HttpMethod.GET,"/sample.html").permitAll()
                .antMatchers(HttpMethod.GET,"/log.html").permitAll()
                .antMatchers(HttpMethod.GET,"/api/referenceType").permitAll()
                .antMatchers(HttpMethod.GET,"/logs/filter/**").permitAll()
                .antMatchers(HttpMethod.GET,"/laboratory.html").permitAll()
                .antMatchers(HttpMethod.GET,"/county.html").permitAll()
                .antMatchers(HttpMethod.GET,"/countyLab.html").permitAll()
                .antMatchers(HttpMethod.GET,"/county").permitAll()
                .antMatchers(HttpMethod.GET,"/hpv.html").permitAll()
                .antMatchers(HttpMethod.GET,"/extHpv.html").permitAll()
                .antMatchers(HttpMethod.GET,"/cell.html").permitAll()
                .antMatchers(HttpMethod.GET,"/municipality.html").permitAll()
                .antMatchers(HttpMethod.GET,"/referenceType.html").permitAll()
                .antMatchers(HttpMethod.GET,"/district.html").permitAll()
                .antMatchers(HttpMethod.GET, "/error_message.html").permitAll()
                .antMatchers(HttpMethod.GET, "/saved_file/**").permitAll()
                .antMatchers(HttpMethod.GET, "/tmp_file/**").permitAll()
                .antMatchers(HttpMethod.GET, "/images/**").permitAll()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/swagger-resources/configuration/ui",
                        "/swagger-ui/**").permitAll()

                .antMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.applyPermitDefaultValues();
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/lib/**", "/api/auth");
    }
}
