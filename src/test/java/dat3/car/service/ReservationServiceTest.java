package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.config.SampleTestConfig;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.reservation.dto.ReservationRequest;
import dat3.car.reservation.dto.ReservationResponse;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;
import dat3.car.reservation.service.ReservationService;

@DataJpaTest
@Import(SampleTestConfig.class)
public class ReservationServiceTest {

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
    List<ReservationRequest> reservationRequestSamples;

    ReservationService reservationService;    

    @BeforeEach
	void beforeEach() {
        reservationService = new ReservationService(reservationRepository, memberRepository, carRepository);
        carSamples = carRepository.saveAll(carSamples);
        memberSamples = memberRepository.saveAll(memberSamples);

        reservationSamples = new ArrayList<Reservation>(Arrays.asList(
            reservationRepository.save(new Reservation(memberSamples.get(0), carSamples.get(0), LocalDate.now().atStartOfDay())),
            reservationRepository.save(new Reservation(memberSamples.get(1), carSamples.get(1), LocalDate.now().atStartOfDay()))
        ));
        reservationRequestSamples = reservationSamples.stream().map(r -> new ReservationRequest(r)).collect(Collectors.toList());
	}

    @AfterEach
    void afterEach() {
        reservationRepository.deleteAll();
		carRepository.deleteAll();
		memberRepository.deleteAll();        
    }
    
    @Test
    @Order(1)
    void testFindAll() {
        List<ReservationResponse> responses = reservationService.findAll();
        
        assertEquals(2, responses.size());
    }

    @Test
    @Order(2)
    void testFind() {
        ReservationResponse response = reservationService.find(reservationSamples.get(0).getId());

        assertEquals(reservationSamples.get(0).getCar().getBrand(), response.getCarBrand());
    }

    @Test
    @Order(3)
    void testCreateWithDateInThePast() {
        ReservationRequest request = new ReservationRequest(
			new Reservation(memberSamples.get(2), carSamples.get(2), LocalDate.now().minusDays(1).atStartOfDay()));

        assertThrows(ResponseStatusException.class, () -> {
			reservationService.create(request);
		});
    }

    @Test
    @Order(4)
    void testCreate() {
        ReservationRequest request = new ReservationRequest(
			new Reservation(memberSamples.get(2), carSamples.get(2), LocalDate.now().plusDays(1).atStartOfDay()));
        ReservationResponse response = reservationService.create(request);

        assertEquals(carSamples.get(2).getBrand(), response.getCarBrand());
    }

    @Test
    @Order(5)
    void testCreateDuplicate() {
        ReservationRequest request = new ReservationRequest(
			new Reservation(memberSamples.get(2), carSamples.get(2), LocalDate.now().plusDays(1).atStartOfDay()));
        reservationService.create(request);

        assertThrows(ResponseStatusException.class, () -> {
			reservationService.create(request);
		});
    }

    @Test
    @Order(6)
    void testUpdate() {
        LocalDate dateTime = LocalDate.now().plusDays(1);
        reservationRequestSamples.get(0).setId(reservationSamples.get(0).getId());
		reservationRequestSamples.get(0).setRentalDate(dateTime);
        reservationService.update(reservationRequestSamples.get(0));

        ReservationResponse response = reservationService.find(reservationSamples.get(0).getId());
        assertEquals(String.format("%sT00:00", dateTime.toString()), response.getRentalDate().toString());
    }

    @Test
    @Order(7)
    void testDelete() {
        reservationService.delete(reservationSamples.get(0).getId());

        assertThrows(ResponseStatusException.class, () -> {
			reservationService.find(reservationSamples.get(0).getId());
		});
    }

    @Test
    @Order(8)
	void findAllByMember() {
		List<ReservationResponse> responses = reservationService.findAllByMember(memberSamples.get(0).getUsername());

        assertEquals(1, responses.size());
	}

	@Test
    @Order(9)
	void countByMember() {
		int count = reservationService.countByMember(memberSamples.get(0).getUsername());

        assertEquals(1, count);
	}
}
