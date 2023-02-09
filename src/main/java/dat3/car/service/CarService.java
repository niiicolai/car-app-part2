package dat3.car.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;

@Service
public class CarService {

    private CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> sampleCars() {
        return Arrays.asList(new Car[] {
            new Car("Toyota", "Camry", 150, 40000),
            new Car("Honda", "Civic", 200, 35000),
            new Car("Tesla", "Model 3", 350, 55000),
            new Car("Ford", "Mustang", 580, 32000),
            new Car("Chevrolet", "Corvette", 480, 75000)
        });
    }

    public List<CarRequest> sampleRequests() {
        return sampleCars().stream().map(car -> new CarRequest(car)).collect(Collectors.toList());
    }
    
    public List<CarResponse> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(car -> new CarResponse(car)).collect(Collectors.toList());
    }

    public CarResponse find(long id) {
        Optional<Car> carOpt = carRepository.findById(id);

        return new CarResponse(carOpt.get());
    }

    public CarResponse create(CarRequest carRequest) {
        Car car = carRepository.save(carRequest.toCar());
        return new CarResponse(car);
    }

    public CarResponse update(CarRequest carRequest) {
        Car car = carRepository.save(carRequest.toCar());
        return new CarResponse(car);
    }

    public void delete(long id) {
        carRepository.deleteById(id);
    }
}
