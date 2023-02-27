package dat3.car.car.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dat3.car.car.dto.CarRequest;
import dat3.car.car.entity.Car;

@Configuration
public class CarSampleConfig {

    // Real number which can be used to make an actual API request.
    private static String realNumber = "DP14440";
    
    // Fake number connected to a fake response to save API requests. 
    private static String fakeNumber = "JN79863";

    @Bean
    public List<Car> carSamples() {
        return Arrays.asList(new Car[] {
            new Car(realNumber, "Toyota", "Camry", 150, 40000),
            new Car(realNumber,"Honda", "Civic", 200, 35000),
            new Car(realNumber,"Tesla", "Model 3", 350, 55000),
            new Car(fakeNumber,"Ford", "Mustang", 580, 32000),
            new Car(fakeNumber,"Chevrolet", "Corvette", 480, 75000),
            new Car(fakeNumber,"Opel", "Kadett", 780, 25400)
        });
    }

    @Bean
    public List<CarRequest> carRequestSamples() {
        return carSamples().stream().map(c -> new CarRequest(c)).collect(Collectors.toList());
    }
}
