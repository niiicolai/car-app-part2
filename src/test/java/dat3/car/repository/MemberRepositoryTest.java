package dat3.car.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
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
public class MemberRepositoryTest {
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

    @BeforeEach
	void beforeEach() {
        carSamples = carRepository.saveAll(carSamples);
        memberSamples = memberRepository.saveAll(memberSamples);

        reservationRepository.save(new Reservation(memberSamples.get(0), carSamples.get(0), LocalDateTime.now()));
	}

	@AfterEach
    void afterEach() {
        reservationRepository.deleteAll();
        memberRepository.deleteAll();
        carRepository.deleteAll();
    }

    @Test
	void testFindAllByReservationsIsNotEmpty() {
		List<Member> members = memberRepository.findAllByReservationsIsNotEmpty();

		assertEquals(1, members.size());
	}
}
