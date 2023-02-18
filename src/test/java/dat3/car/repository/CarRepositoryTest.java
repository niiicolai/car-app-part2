package dat3.car.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.config.SampleTestConfig;
import dat3.car.member.repository.MemberRepository;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import(SampleTestConfig.class)
public class CarRepositoryTest {
    @Autowired
	ReservationRepository reservationRepository;

    @Autowired
	MemberRepository memberRepository;

    @Autowired
	CarRepository carRepository;

    @Autowired 
    List<Reservation> reservationSamples;

    @BeforeAll
	void beforeAll() {
        int i = 0;
        for (Reservation sample : reservationSamples) {
            carRepository.save(sample.getCar());
			memberRepository.save(sample.getMember());
            Reservation saved = reservationRepository.save(sample);
            reservationSamples.set(i, saved);
            i++;
        }

        reservationRepository.deleteById(reservationSamples.get(reservationSamples.size() - 1).getId());
	}

	@AfterAll
    void afterAll() {
        reservationRepository.deleteAll();
        memberRepository.deleteAll();
        carRepository.deleteAll();
    }

	@Test
	void testFindCarByBrandAndModel() {
        Car carSample = reservationSamples.get(0).getCar();
		List<Car> cars = carRepository.findAllByBrandAndModel(carSample.getBrand(), carSample.getModel());

		assertEquals(1, cars.size());
        assertEquals(carSample.getBrand(), cars.get(0).getBrand());
        assertEquals(carSample.getModel(), cars.get(0).getModel());
	}

    @Test
	void testFindAveragePricePerDay() {
        double expectedAverage = 0;
        for (Reservation sample : reservationSamples) {
            expectedAverage += sample.getCar().getPricePrDay();
        }
        expectedAverage /= reservationSamples.size();
		double average = carRepository.findAveragePricePrDay();

		assertEquals(expectedAverage, average);
	}

    @Test
	void testFindAllWithBestDiscount() {
        Car carSample = reservationSamples.get(reservationSamples.size() - 1).getCar();
		List<Car> cars = carRepository.findAllWithBestDiscount();

		assertEquals(1, cars.size());
        assertEquals(carSample.getBrand(), cars.get(0).getBrand());
        assertEquals(carSample.getModel(), cars.get(0).getModel());
	}

    @Test
	void testFindAllByReservationsIsEmpty() {
		List<Car> cars = carRepository.findAllByReservationsIsEmpty();

		assertEquals(1, cars.size());
	}
}
