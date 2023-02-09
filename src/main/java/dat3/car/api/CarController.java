package dat3.car.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.service.CarService;

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
    public CarResponse find(@PathVariable("id") long id) {
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
    public void delete(@PathVariable("id") long id) {
        carService.delete(id);
    }
}
