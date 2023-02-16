package dat3.car.dto.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.entity.Car;
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
    private String brand;

    @JsonProperty
    private String model;

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
        brand = car.getBrand();
        model = car.getModel();
        pricePrDay = car.getPricePrDay();
        bestDiscount = car.getBestDiscount();
        created = car.getCreated();
        lastEdited = car.getLastEdited();
    }
}
