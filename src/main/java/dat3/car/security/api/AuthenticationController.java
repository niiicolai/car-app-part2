package dat3.car.security.api;

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
    
    // Role: ANONYMOUS
    @PostMapping
    public LoginResponse authenticate(@RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }
}
