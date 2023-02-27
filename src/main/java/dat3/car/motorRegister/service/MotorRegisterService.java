package dat3.car.motorRegister.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MotorRegisterService {
    
    @Value("${motor.api.key}")
    private String apiKey;

}
