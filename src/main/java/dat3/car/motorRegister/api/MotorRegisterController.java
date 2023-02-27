package dat3.car.motorRegister.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dat3.car.motorRegister.dto.MotorRegisterResponse;
import dat3.car.motorRegister.service.MotorRegisterService;

@RestController
@RequestMapping("/api/v1/motor/register")
public class MotorRegisterController {

    private MotorRegisterService motorRegisterService;

    public MotorRegisterController(MotorRegisterService motorRegisterService)
    {
        this.motorRegisterService = motorRegisterService;
    }
    
    @GetMapping("/{registrationNumber}")
    public List<MotorRegisterResponse> lookup(@PathVariable("registrationNumber") String registrationNumber) {
        return motorRegisterService.lookup(registrationNumber);
    }
}
