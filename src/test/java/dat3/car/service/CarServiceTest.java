package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import dat3.car.car.dto.CarRequest;
import dat3.car.car.dto.CarResponse;
import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.car.service.CarService;
import dat3.car.config.SampleTestConfig;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Import(SampleTestConfig.class)
public class CarServiceTest {

	@Autowired
	CarRepository carRepository;	

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired 
    List<Car> carSamples;

	@Autowired 
    List<Member> memberSamples;
	
	List<CarRequest> carRequestSamples;
	CarService carService;

    List<Reservation> reservationSamples;

	@BeforeEach
	void beforeEach() {
		carService = new CarService(carRepository);

		carSamples.get(carSamples.size() - 1).setBestDiscount(999999);
        carSamples = carRepository.saveAll(carSamples);
		carRequestSamples = carSamples.stream().map(r -> new CarRequest(r)).collect(Collectors.toList());
        memberSamples = memberRepository.saveAll(memberSamples);

        reservationSamples = new ArrayList<Reservation>(Arrays.asList(
            reservationRepository.save(new Reservation(memberSamples.get(0), carSamples.get(0), LocalDateTime.now())),
            reservationRepository.save(new Reservation(memberSamples.get(1), carSamples.get(1), LocalDateTime.now()))
        ));
	}

	@AfterEach
    void afterEach() {
        reservationRepository.deleteAll();
        memberRepository.deleteAll();
        carRepository.deleteAll();
    }

	@Test
	void testFindAll() {
		List<CarResponse> responses = carService.findAll();

		assertEquals(carSamples.size(), responses.size());
	}

	@Test
	void testFind() {
		CarResponse response = carService.find(carSamples.get(0).getId());

		assertEquals(carSamples.get(0).getMake(), response.getMake());
	}

	@Test
	void testCreate() {
		CarResponse response = carService.create(carRequestSamples.get(2));

		assertEquals(carSamples.get(2).getMake(), response.getMake());
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
		List<CarResponse> responses = carService.findAllByMakeAndModel(
			carSamples.get(0).getMake(), carSamples.get(0).getModel()
		);

		assertEquals(1, responses.size());
		assertEquals(carSamples.get(0).getMake(), responses.get(0).getMake());
		assertEquals(carSamples.get(0).getModel(), responses.get(0).getModel());
	}

	@Test
	void testFindAveragePricePrDay() {
		double expectedAverage = 0;
		for(Car sample : carSamples) {
			expectedAverage += sample.getPricePrDay();
		}
        expectedAverage /= carSamples.size();
		double average = carRepository.findAveragePricePrDay();

		assertEquals(expectedAverage, average);
	}

	@Test
	void testFindAllWithBestDiscount() {
		List<CarResponse> responses = carService.findAllWithBestDiscount();

		assertEquals(1, responses.size());
		assertEquals(carSamples.get(carSamples.size() - 1).getId(), responses.get(0).getId());
	}

	@Test
	void testFindAllByReservationsIsEmpty() {
		List<CarResponse> responses = carService.findAllByReservationsIsEmpty();

		assertEquals(carSamples.size() - 2, responses.size());
	}
}
