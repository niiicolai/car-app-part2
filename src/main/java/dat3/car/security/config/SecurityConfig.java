package dat3.car.security.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import dat3.car.security.service.MemberDetailsService;
import java.util.Collections;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
public class SecurityConfig {

  @Autowired
  private MemberDetailsService memberDetailsService;

  private static final String[] unauthorizedPaths = {
    "/",
    "/api/v1/authenticate",
    "/error",
  };

  private static final String[] memberPaths = {
    "/api/v1/cars",
    "/api/v1/members",
    "/api/v1/reservations",
  };

  private static final String[] adminPaths = {
    "/api/v1/cars**",
    "/api/v1/members**",
    "/api/v1/reservations**",
  };

  @Value("${app.secret-key}")
  private String tokenSecret;

  /*
    @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return (web) -> web.ignoring()
            // Spring Security should completely ignore URLs starting with /resources/
                    .requestMatchers("/**", "/api/v1/authenticate");
        } */

  @Bean
  public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      
      .authorizeHttpRequests(authorize ->
        authorize.requestMatchers(unauthorizedPaths).permitAll()
      )
      .oauth2ResourceServer()
      .jwt()
      .jwtAuthenticationConverter(authenticationConverter());
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
