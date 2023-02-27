package dat3.car.motorRegister.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.motorRegister.entity.MotorRegister;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MotorRegisterResponse {
    @JsonProperty("registration_number")
    private String registrationNumber;
    
    @JsonProperty("status")
    private String status; 
    
    @JsonProperty("status_date")
    private String statusDate;
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("use")
    private String use;
    
    @JsonProperty("first_registration")
    private String firstRegistration;
    
    @JsonProperty("vin")
    private String vin;
    
    @JsonProperty("doors")
    private int doors;
    
    @JsonProperty("make")
    private String make;
    
    @JsonProperty("model")
    private String model;
    
    @JsonProperty("variant")
    private String variant;
    
    @JsonProperty("model_type")
    private String modelType;
    
    @JsonProperty("color")
    private String color;
    
    @JsonProperty("fuel_type")
    private String fuelType;
    
    @JsonProperty("is_leasing")
    private boolean isLeasing;
    
    @JsonProperty("leasing_from")
    private String leasingFrom;
    
    @JsonProperty("leasing_to")
    private String leasingTo;

    public MotorRegisterResponse(MotorRegister motorRegister)
    {
        registrationNumber = motorRegister.getRegistrationNumber();
        status = motorRegister.getStatus();
        statusDate = motorRegister.getStatusDate();
        type = motorRegister.getType();
        use = motorRegister.getUse();
        firstRegistration = motorRegister.getFirstRegistration();
        vin = motorRegister.getVin();
        doors = motorRegister.getDoors();
        make = motorRegister.getMake();
        model = motorRegister.getModel();
        variant = motorRegister.getVariant();
        modelType = motorRegister.getModelType();
        color = motorRegister.getColor();
        fuelType = motorRegister.getFuelType();
        isLeasing = motorRegister.isLeasing();
        leasingFrom = motorRegister.getLeasingFrom();
        leasingTo = motorRegister.getLeasingTo();
    }

    public MotorRegister toMotorRegister()
    {
        return MotorRegister.builder()
            .registrationNumber(registrationNumber)
            .status(status)
            .statusDate(statusDate)
            .type(type)
            .use(use)
            .firstRegistration(firstRegistration)
            .vin(vin)
            .doors(doors)
            .make(make)
            .model(model)
            .variant(variant)
            .modelType(modelType)
            .color(color)
            .fuelType(fuelType)
            .isLeasing(isLeasing)
            .leasingFrom(leasingFrom)
            .leasingTo(leasingTo)
            .build();
    }
}
