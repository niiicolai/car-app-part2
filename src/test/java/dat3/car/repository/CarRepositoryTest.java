package dat3.car.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.config.SampleTestConfig;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

@DataJpaTest
@Import(SampleTestConfig.class)
public class CarRepositoryTest {
    @Autowired
	ReservationRepository reservationRepository;

    @Autowired
	MemberRepository memberRepository;

    @Autowired
	CarRepository carRepository;

    @Autowired 
    List<Car> carSamples;

    @Autowired 
    List<Member> memberSamples;

    List<Reservation> reservationSamples;

    @BeforeEach
	void beforeEach() {
        carSamples.get(carSamples.size() - 1).setBestDiscount(999999);
        carSamples = carRepository.saveAll(carSamples);
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
	void testFindCarByBrandAndModel() {
		List<Car> cars = carRepository.findAllByMakeAndModel(carSamples.get(0).getMake(), carSamples.get(0).getModel());

		assertEquals(1, cars.size());
        assertEquals(carSamples.get(0).getMake(), cars.get(0).getMake());
        assertEquals(carSamples.get(0).getModel(), cars.get(0).getModel());
	}

    @Test
	void testFindAveragePricePerDay() {
        double expectedAverage = 0;
        for (Car sample : carSamples) {
            expectedAverage += sample.getPricePrDay();
        }
        expectedAverage /= carSamples.size();
		double average = carRepository.findAveragePricePrDay();

		assertEquals(expectedAverage, average);
	}

    @Test
	void testFindAllWithBestDiscount() {
        Car carSample = carSamples.get(carSamples.size() - 1);
		List<Car> cars = carRepository.findAllWithBestDiscount();

		assertEquals(1, cars.size());
        assertEquals(carSample.getMake(), cars.get(0).getMake());
        assertEquals(carSample.getModel(), cars.get(0).getModel());
	}

    @Test
	void testFindAllByReservationsIsEmpty() {
		List<Car> cars = carRepository.findAllByReservationsIsEmpty();

		assertEquals(carSamples.size() - 2, cars.size());
	}
}
