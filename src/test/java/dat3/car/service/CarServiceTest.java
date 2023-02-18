package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dat3.car.car.dto.CarRequest;
import dat3.car.car.dto.CarResponse;
import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.car.service.CarService;
import dat3.car.config.SampleTestConfig;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ResponseStatusException;
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

		// Ensure number two sample has the highest discount
		carSamples.get(1).setBestDiscount(99999999);

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

		assertThrows(ResponseStatusException.class, () -> {
			carService.find(carSamples.get(0).getId());
		});
	}

	@Test
	void testFindAllByBrandAndModel() {
		List<CarResponse> responses = carService.findAllByBrandAndModel(
			carSamples.get(0).getBrand(), carSamples.get(0).getModel()
		);

		assertEquals(1, responses.size());
		assertEquals(carSamples.get(0).getBrand(), responses.get(0).getBrand());
		assertEquals(carSamples.get(0).getModel(), responses.get(0).getModel());
	}

	@Test
	void testFindAveragePricePrDay() {
		double expectedAverage = 0;
        expectedAverage += carSamples.get(0).getPricePrDay();
		expectedAverage += carSamples.get(1).getPricePrDay();
        expectedAverage /= 2;
		double average = carRepository.findAveragePricePrDay();

		assertEquals(expectedAverage, average);
	}

	@Test
	void testFindAllWithBestDiscount() {
		List<CarResponse> responses = carService.findAllWithBestDiscount();

		assertEquals(1, responses.size());
		assertEquals(carSamples.get(1).getId(), responses.get(0).getId());
	}

	@Test
	void testFindAllByReservationsIsEmpty() {
		// Ideal: Add an reservation to one of the cars.
		List<CarResponse> responses = carService.findAllByReservationsIsEmpty();

		assertEquals(2, responses.size());
	}
}
