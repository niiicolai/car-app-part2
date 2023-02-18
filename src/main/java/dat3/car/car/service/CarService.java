package dat3.car.car.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dat3.car.car.dto.CarRequest;
import dat3.car.car.dto.CarResponse;
import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;

@Service
public class CarService {

    private CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
    
    public List<CarResponse> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(car -> new CarResponse(car)).collect(Collectors.toList());
    }

    public CarResponse find(int id) {
        Optional<Car> carOpt = carRepository.findById(id);
        if (carOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with <ID> doesn't exist!");

        return new CarResponse(carOpt.get());
    }

    public CarResponse create(CarRequest carRequest) {
        Car car = carRepository.save(carRequest.toCar());
        return new CarResponse(car);
    }

    public CarResponse update(CarRequest carRequest) {
        Optional<Car> carOpt = carRepository.findById(carRequest.getId());
        if (carOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with <ID> doesn't exist!");
        
        Car car = carOpt.get();
        car.setBrand(carRequest.getBrand());
        car.setModel(carRequest.getModel());
        car.setBestDiscount(carRequest.getBestDiscount());
        car.setPricePrDay(carRequest.getPricePrDay());
        car = carRepository.save(car);

        return new CarResponse(car);
    }

    public void delete(int id) {
        Optional<Car> carOpt = carRepository.findById(id);
        if (carOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car with <ID> doesn't exist!");

        carRepository.delete(carOpt.get());
    }

    public List<CarResponse> findAllByBrandAndModel(String brand, String model) {
        List<Car> cars = carRepository.findAllByBrandAndModel(brand, model);
        return cars.stream().map(car -> new CarResponse(car)).collect(Collectors.toList());
    }

    public Double findAveragePricePrDay() {
        return carRepository.findAveragePricePrDay();
    }
    
    public List<CarResponse> findAllWithBestDiscount() {
        List<Car> cars = carRepository.findAllWithBestDiscount();
        return cars.stream().map(car -> new CarResponse(car)).collect(Collectors.toList());
    }

    public List<CarResponse> findAllByReservationsIsEmpty() {
        List<Car> cars = carRepository.findAllByReservationsIsEmpty();
        return cars.stream().map(car -> new CarResponse(car)).collect(Collectors.toList());
    }
}
