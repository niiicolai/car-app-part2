package dat3.car.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import dat3.car.config.SampleTestConfig;
import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import(SampleTestConfig.class)
public class ReservationRepositoryTest {

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
        for (int i = 0; i < reservationSamples.size(); i++) {
			Car car = carRepository.save(reservationSamples.get(i).getCar());
			Member member = memberRepository.save(reservationSamples.get(i).getMember());
			
			reservationSamples.get(i).setCar(car);
			reservationSamples.get(i).setMember(member);
		}

        reservationSamples.get(0).setId(reservationRepository.save(reservationSamples.get(0)).getId());
	}

	@AfterAll
    void afterAll() {
        reservationRepository.deleteAll();
        memberRepository.deleteAll();
        carRepository.deleteAll();
    }

	@Test
	void testFindAllByMember() {
        // Member with reservation
        Optional<Member> memberWithReservation = memberRepository.findById(reservationSamples.get(0).getMember().getUsername());
		List<Reservation> reservations1 = reservationRepository.findAllByMember(memberWithReservation.get());

        // Member without reservation
        Optional<Member> memberWithoutReservation = memberRepository.findById(reservationSamples.get(1).getMember().getUsername());
		List<Reservation> reservations2 = reservationRepository.findAllByMember(memberWithoutReservation.get());

		assertEquals(1, reservations1.size());
        assertEquals(reservationSamples.get(0).getId(), reservations1.get(0).getId());

        assertEquals(0, reservations2.size());
	}

    @Test
	void testCountByMember() {
        // Member with reservation
        Optional<Member> memberWithReservation = memberRepository.findById(reservationSamples.get(0).getMember().getUsername());
		int count1 = reservationRepository.countByMember(memberWithReservation.get());

        // Member without reservation
        Optional<Member> memberWithoutReservation = memberRepository.findById(reservationSamples.get(1).getMember().getUsername());
		int count2 = reservationRepository.countByMember(memberWithoutReservation.get());

		assertEquals(1, count1);
        assertEquals(0, count2);
	}
}
