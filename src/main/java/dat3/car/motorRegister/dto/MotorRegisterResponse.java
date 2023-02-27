package dat3.car.motorRegister.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MotorRegisterResponse {
    @JsonProperty private String registrationNumber;

    @JsonProperty private String state;
    
    @JsonProperty private LocalDateTime stateDate;

    @JsonProperty private String type;

    @JsonProperty private String use;

    @JsonProperty private LocalDateTime firstRegistration;

    @JsonProperty private String vin;

    @JsonProperty private int doors;

    @JsonProperty private String make;

    @JsonProperty private String model;

    @JsonProperty private String variant;

    @JsonProperty private String modelType;

    @JsonProperty private String color;

    @JsonProperty private String fuelType;

    @JsonProperty private boolean isLeasing;

    @JsonProperty private LocalDateTime leasingFrom;

    @JsonProperty private LocalDateTime leasingTo;
}
