package dat3.car.security.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;

import dat3.car.security.error.CustomOAuth2AccessDeniedHandler;
import dat3.car.security.error.CustomOAuth2AuthenticationEntryPoint;
import dat3.car.security.service.MemberDetailsService;
import java.util.Collections;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

//https://docs.spring.io/spring-security/reference/servlet/configuration/java.html

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private MemberDetailsService memberDetailsService;

    @Value("${app.secret-key}")
    private String tokenSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //This line is added to make the h2-console work (if needed)
        http.headers().frameOptions().disable();
        http
                .cors().and().csrf().disable()

                .httpBasic(Customizer.withDefaults())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //REF: https://mflash.dev/post/2021/01/19/error-handling-for-spring-security-resource-server/
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(new CustomOAuth2AuthenticationEntryPoint())
                        .accessDeniedHandler(new CustomOAuth2AccessDeniedHandler())
                )
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(authenticationConverter());

        http.authorizeHttpRequests((authorize) -> authorize
                //Obviously we need to be able to login without being logged in :-)
                .requestMatchers(HttpMethod.POST, "/api/v1/authenticate").permitAll()

                //Required in order to use the h2-console
                .requestMatchers("/h2*/**").permitAll()

                .requestMatchers("/").permitAll()
                .requestMatchers("/index.html").permitAll() //Allow for a default index.html file
                .requestMatchers("/favicon.ico").permitAll() //Allow for a default favicon.ico file
                .requestMatchers("/src/**").permitAll() // permit javascript stuff

                //necessary to allow for "nice" JSON Errors
                .requestMatchers("/error").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/v1/cars").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/cars/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/cars").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/cars").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/cars/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/v1/members").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/members/**").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/members/no-reservations").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/members").permitAll()
                .requestMatchers(HttpMethod.PATCH, "/api/v1/members").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/members/**").hasRole("ADMIN")
                

                .requestMatchers(HttpMethod.GET, "/api/v1/reservations").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/reservations/**").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/reservations/find-all-by-member/**").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/reservations/count-by-member/**").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/reservations").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/reservations").hasAnyRole("MEMBER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/reservations/**").hasRole("ADMIN")
                
                //.requestMatchers(adminPaths).hasRole("ADMIN")

                //.requestMatchers("/", "/**").permitAll());

            // .requestMatchers(HttpMethod.GET,"/api/demo/anonymous").permitAll());

            // Demonstrates another way to add roles to an endpoint
            // .requestMatchers(HttpMethod.GET, "/api/demo/admin").hasAuthority("ADMIN")
        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter authenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
        jwtGrantedAuthoritiesConverter
        );
        return jwtAuthenticationConverter;
    }

    public void configureGlobal(AuthenticationManagerBuilder auth)
        throws Exception {
        auth
        .userDetailsService(memberDetailsService)
        .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(
        Collections.singletonList(authenticationProvider())
        );
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(memberDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey secretKey() {
        return new SecretKeySpec(tokenSecret.getBytes(), "HmacSHA256");
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withSecretKey(secretKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(
        new ImmutableSecret<SecurityContext>(secretKey())
        );
    }
}
