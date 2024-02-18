package FitnessBro.apiPayload.configuration;

import FitnessBro.service.MemberService.MemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberCommandService memberCommandService;

    @Value("${jwt.secret}")
    private String seceretKey;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/members/sign-up").permitAll()
                                .requestMatchers("/members/login").permitAll()
                                .requestMatchers("/coaches/sign-up").permitAll()
                                .requestMatchers("/coaches/login").permitAll()
                                .requestMatchers("/login/oauth2/code/**").permitAll()
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()

                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/login/oauth2/code/kakao/*"))
                )
                .addFilterBefore(new JwtFilter(memberCommandService, seceretKey), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }




}
//.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//
//        return httpSecurity.build();
//    }
//


//}
//
//class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
//            throws IOException, ServletException {
//        response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.getWriter().write("{\"code\": \"NP\", \"message\": \"No Permission.\"}");
//    }
//}