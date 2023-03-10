package dat3.car.security.api;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dat3.car.security.dto.LoginRequest;
import dat3.car.security.dto.LoginResponse;
import dat3.car.security.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/authenticate")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // Role: USER
    @GetMapping()
    public LoginResponse authenticated(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        List<String> authorities = jwt.getClaimAsStringList("roles");
        return new LoginResponse(username, "", authorities);
    }
    
    // Role: ANONYMOUS
    @PostMapping
    public LoginResponse authenticate(@RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }
}
