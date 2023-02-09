package dat3.car.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dat3.car.entity.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequest {

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

    public CarRequest(Car car) {
        id = car.getId();
        brand = car.getBrand();
        model = car.getModel();
        pricePrDay = car.getPricePrDay();
        bestDiscount = car.getBestDiscount();
    }

    public Car toCar() {
        return new Car(brand, model, pricePrDay, bestDiscount);
    }
}
