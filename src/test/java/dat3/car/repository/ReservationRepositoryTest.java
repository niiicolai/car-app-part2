package dat3.car.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
public class ReservationRepositoryTest {

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
        carSamples = carRepository.saveAll(carSamples);
        memberSamples = memberRepository.saveAll(memberSamples);

        reservationSamples = new ArrayList<Reservation>(Arrays.asList(
            reservationRepository.save(new Reservation(memberSamples.get(0), carSamples.get(0), LocalDate.now().atStartOfDay()))
        ));
	}

	@AfterEach
    void afterEach() {
        reservationRepository.deleteAll();
        memberRepository.deleteAll();
        carRepository.deleteAll();
    }

	@Test
    @Order(1)
	void testFindAllByMember() {
        // Member with reservation
        Optional<Member> memberWithReservation = memberRepository.findById(memberSamples.get(0).getUsername());
		List<Reservation> reservations1 = reservationRepository.findAllByMember(memberWithReservation.get());

        // Member without reservation
        Optional<Member> memberWithoutReservation = memberRepository.findById(memberSamples.get(1).getUsername());
		List<Reservation> reservations2 = reservationRepository.findAllByMember(memberWithoutReservation.get());
        
		assertEquals(1, reservations1.size());
        assertEquals(reservationSamples.get(0).getId(), reservations1.get(0).getId());

        assertEquals(0, reservations2.size());
	}

    @Test
    @Order(2)
	void testCountByMember() {
        // Member with reservation
        Optional<Member> memberWithReservation = memberRepository.findById(memberSamples.get(0).getUsername());
		int count1 = reservationRepository.countByMember(memberWithReservation.get());

        // Member without reservation
        Optional<Member> memberWithoutReservation = memberRepository.findById(memberSamples.get(1).getUsername());
		int count2 = reservationRepository.countByMember(memberWithoutReservation.get());

		assertEquals(1, count1);
        assertEquals(0, count2);
	}

    @Test
    @Order(3)
	void testExistByCarAndRentalDate() {
        Optional<Car> carOpt = carRepository.findById(carSamples.get(0).getId());

        boolean shouldExist = reservationRepository.existsByCarAndRentalDate(
            carOpt.get(), reservationSamples.get(0).getRentalDate());
        
        boolean shouldNotExist = reservationRepository.existsByCarAndRentalDate(
            carOpt.get(), reservationSamples.get(0).getRentalDate().plusDays(1));

        assertTrue(shouldExist);
        assertTrue(!(shouldNotExist));
	}
}
