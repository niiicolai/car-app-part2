package dat3.car.car.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dat3.car.application.dto.ErrorResponse;
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
    public Object find(@PathVariable("id") int id) {
        try {
            return carService.find(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: ADMIN
    @PostMapping
    public CarResponse create(@RequestBody CarRequest carRequest) {
        return carService.create(carRequest);
    }

    // Role: ADMIN
    @PatchMapping
    public Object update(@RequestBody CarRequest carRequest) {
        try {
            return carService.update(carRequest);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: ADMIN
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") int id) {
        try {
            carService.delete(id);
            return ResponseEntity.ok();
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: ANONYMOUS
    @GetMapping("/by/{make}/{model}")
    public List<CarResponse> findAllByBrandAndModel(@PathVariable("make") String make, 
            @PathVariable("model") String model) {
        return carService.findAllByMakeAndModel(make, model);
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
