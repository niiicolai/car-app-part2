package dat3.car.car.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.car.entity.Car;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarResponse {

    @JsonProperty
    private int id;

    @JsonProperty
    private String make;

    @JsonProperty
    private String model;

    @JsonProperty
    private String registrationNumber;

    @JsonProperty
    private double pricePrDay;

    @JsonProperty
    private int bestDiscount;

    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDateTime lastEdited;

    public CarResponse(Car car) {
        id = car.getId();
        make = car.getMake();
        model = car.getModel();
        pricePrDay = car.getPricePrDay();
        bestDiscount = car.getBestDiscount();
        created = car.getCreated();
        lastEdited = car.getLastEdited();
        registrationNumber = car.getRegistrationNumber();
    }
}
