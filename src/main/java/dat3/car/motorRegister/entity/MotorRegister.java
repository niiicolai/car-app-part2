package dat3.car.motorRegister.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter 
@Setter 
public class MotorRegister {
    
    private String registrationNumber;

    private String state;
    
    private LocalDateTime stateDate;

    private String type;

    private String use;

    private LocalDateTime firstRegistration;

    private String vin;

    private int doors;

    protected String make;

    protected String model;

    private String variant;

    private String modelType;

    private String color;

    private String fuelType;

    private boolean isLeasing;

    private LocalDateTime leasingFrom;

    private LocalDateTime leasingTo;
}
