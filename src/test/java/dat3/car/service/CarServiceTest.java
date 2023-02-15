package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dat3.car.config.SampleTestConfig;
import dat3.car.dto.car.CarRequest;
import dat3.car.dto.car.CarResponse;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import(SampleTestConfig.class)
public class CarServiceTest {

	@Autowired
	CarRepository carRepository;	

	@Autowired 
    List<Car> carSamples;
	
	@Autowired 
	List<CarRequest> carRequestSamples;

	CarService carService;

	@BeforeAll
	void beforeAll() {
		carService = new CarService(carRepository);

		carSamples.get(0).setId(carRepository.save(carSamples.get(0)).getId());
		carSamples.get(1).setId(carRepository.save(carSamples.get(1)).getId());
	}

	@AfterAll
    void afterAll() {
        carRepository.deleteAll();
    }

	@Test
	void testFindAll() {
		List<CarResponse> responses = carService.findAll();

		assertEquals(2, responses.size());
	}

	@Test
	void testFind() {
		CarResponse response = carService.find(carSamples.get(0).getId());

		assertEquals(carSamples.get(0).getBrand(), response.getBrand());
	}

	@Test
	void testCreate() {
		CarResponse response = carService.create(carRequestSamples.get(2));

		assertEquals(carSamples.get(2).getBrand(), response.getBrand());
	}

	@Test
	void testUpdate() {
		carRequestSamples.get(0).setId(carSamples.get(0).getId());
		carRequestSamples.get(0).setPricePrDay(55500);

		CarResponse response = carService.update(carRequestSamples.get(0));

		assertEquals(
			carRequestSamples.get(0).getPricePrDay(),
			response.getPricePrDay()
		);
	}

	@Test
	void testDelete() {
		carService.delete(carSamples.get(0).getId());

		assertThrows(NoSuchElementException.class, () -> {
			carService.find(carSamples.get(0).getId());
		});
	}
}
