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
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import(SampleTestConfig.class)
public class MemberRepositoryTest {
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
	void testFindAllByReservationsIsNotEmpty() {
		List<Member> members = memberRepository.findAllByReservationsIsNotEmpty();

		assertEquals(1, members.size());
        assertEquals(true, members.size() < reservationSamples.size());
	}
}
