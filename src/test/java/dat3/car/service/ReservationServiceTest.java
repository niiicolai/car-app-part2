package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
@TestInstance(Lifecycle.PER_CLASS)
@Import(SampleTestConfig.class)
public class ReservationServiceTest {

    @Autowired 
    ReservationRepository reservationRepository;

    @Autowired
	MemberRepository memberRepository;

    @Autowired
	CarRepository carRepository;    

    @Autowired 
    List<Reservation> reservationSamples;
    
    @Autowired 
    List<ReservationRequest> reservationRequestSamples;

    ReservationService reservationService;    

    @BeforeAll
    void beforeAll() {
        reservationService = new ReservationService(reservationRepository, memberRepository, carRepository);

		for (int i = 0; i < reservationSamples.size(); i++) {
			Car car = carRepository.save(reservationSamples.get(i).getCar());
			Member member = memberRepository.save(reservationSamples.get(i).getMember());
			
			reservationSamples.get(i).setCar(car);
			reservationSamples.get(i).setMember(member);            
			reservationRequestSamples.get(i).setCarId(car.getId());
		}

        reservationSamples.get(0).setId(reservationRepository.save(reservationSamples.get(0)).getId());
		reservationSamples.get(1).setId(reservationRepository.save(reservationSamples.get(1)).getId());
    }

    @AfterAll
    void afterAll() {
        reservationRepository.deleteAll();
		carRepository.deleteAll();
		memberRepository.deleteAll();        
    }
    
    @Test
    void testFindAll() {
        List<ReservationResponse> responses = reservationService.findAll();
        
        assertEquals(2, responses.size());
    }

    @Test
    void testFind() {
        ReservationResponse response = reservationService.find(reservationSamples.get(0).getId());

        assertEquals(reservationSamples.get(0).getCar().getBrand(), response.getCarBrand());
    }

    @Test
    void testCreate() {
        ReservationResponse response = reservationService.create(reservationRequestSamples.get(2));

        assertEquals(reservationSamples.get(2).getCar().getBrand(), response.getCarBrand());
    }

    @Test
    void testUpdate() {
        LocalDate dateTime = LocalDate.parse("2012-12-12");
        reservationRequestSamples.get(0).setId(reservationSamples.get(0).getId());
		reservationRequestSamples.get(0).setRentalDate(dateTime);
        reservationService.update(reservationRequestSamples.get(0));

        ReservationResponse response = reservationService.find(reservationSamples.get(0).getId());
        assertEquals("2012-12-12T00:00", response.getRentalDate().toString());
    }

    @Test
    void testDelete() {
        reservationService.delete(reservationSamples.get(0).getId());

        assertThrows(ResponseStatusException.class, () -> {
			reservationService.find(reservationSamples.get(0).getId());
		});
    }

    @Test
	void findAllByMember() {
		
	}

	@Test
	void countByMember() {
		
	}
}
