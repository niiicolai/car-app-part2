package dat3.car.car.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dat3.car.car.dto.CarRequest;
import dat3.car.car.dto.CarResponse;
import dat3.car.car.service.CarService;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    // Role: ANONYMOUS
    @GetMapping
    public List<CarResponse> findAll() {
        return carService.findAll();
    }

    // Role: ANONYMOUS
    @GetMapping("/{id}")
    public CarResponse find(@PathVariable("id") int id) {
        return carService.find(id);
    }

    // Role: ADMIN
    @PostMapping
    public CarResponse create(@RequestBody CarRequest carRequest) {
        return carService.create(carRequest);
    }

    // Role: ADMIN
    @PatchMapping
    public CarResponse update(@RequestBody CarRequest carRequest) {
        return carService.update(carRequest);
    }

    // Role: ADMIN
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        carService.delete(id);
    }

    // Role: ANONYMOUS
    @GetMapping("/by/{brand}/{model}")
    public List<CarResponse> findAllByBrandAndModel(@PathVariable("brand") String brand, 
            @PathVariable("model") String model) {
        return carService.findAllByBrandAndModel(brand, model);
    }

    // Role: ANONYMOUS
    @GetMapping("/average-price-pr-day")
    public Double findAveragePricePrDay() {
        return carService.findAveragePricePrDay();
    }
    
    // Role: ANONYMOUS
    @GetMapping("/best-discount")
    public List<CarResponse> findAllWithBestDiscount() {
        return carService.findAllWithBestDiscount();
    }

    // Role: ANONYMOUS
    @GetMapping("/no-reservations")
    public List<CarResponse> findAllByReservationsIsEmpty() {
        return carService.findAllByReservationsIsEmpty();
    }
}
