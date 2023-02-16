package dat3.security.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.joining;

import dat3.security.dto.LoginRequest;
import dat3.security.dto.LoginResponse;
import dat3.security.entity.UserWithRoles;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JwtEncoder jwtEncoder;

    @Value("${app.token-issuer}")
    private String tokenIssuer;

    @Value("${app.token-expiration}")
    private long tokenExpiration;
    
    public LoginResponse authenticate(LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()); 
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        UserWithRoles user = (UserWithRoles) authentication.getPrincipal();
        String token = getToken(authentication, user);

        return new LoginResponse(user.getUsername(), token, user.getRoles());
    }

    private String getToken(Authentication authentication, UserWithRoles user) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(tokenIssuer)  //Only this for simplicity
                .issuedAt(now)
                .expiresAt(now.plusSeconds(tokenExpiration))
                .subject(user.getUsername())
                .claim("roles",scope)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
