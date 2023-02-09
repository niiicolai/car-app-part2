package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class CarServiceTest {

	@Autowired
	CarRepository repository;

	CarService service;

	List<Car> samples;
	
	List<CarRequest> sampleRequests;

	@BeforeAll
	void beforeAll() {
		service = new CarService(repository);
		samples = service.sampleCars();
		sampleRequests = service.sampleRequests();

		samples.get(0).setId(repository.save(samples.get(0)).getId());
		samples.get(1).setId(repository.save(samples.get(1)).getId());
	}

	@AfterAll
    void afterAll() {
        repository.deleteAll();
    }

	@Test
	void testFindAll() {
		List<CarResponse> responses = service.findAll();

		assertEquals(2, responses.size());
	}

	@Test
	void testFind() {
		CarResponse response = service.find(samples.get(0).getId());

		assertEquals(samples.get(0).getBrand(), response.getBrand());
	}

	@Test
	void testCreate() {
		CarResponse response = service.create(sampleRequests.get(2));

		assertEquals(samples.get(2).getBrand(), response.getBrand());
	}

	@Test
	void testUpdate() {
		sampleRequests.get(0).setId(samples.get(0).getId());
		sampleRequests.get(0).setPricePrDay(55500);

		CarResponse response = service.update(sampleRequests.get(0));

		assertEquals(
			sampleRequests.get(0).getPricePrDay(),
			response.getPricePrDay()
		);
	}

	@Test
	void testDelete() {
		service.delete(samples.get(0).getId());

		assertThrows(NoSuchElementException.class, () -> {
			service.find(samples.get(0).getId());
		});
	}
}
