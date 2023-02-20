package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.config.SampleTestConfig;
import dat3.car.config.SecurityTestConfig;
import dat3.car.member.dto.MemberRequest;
import dat3.car.member.dto.MemberResponse;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.member.service.MemberService;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

@DataJpaTest
@Import({SampleTestConfig.class, SecurityTestConfig.class})
public class MemberServiceTest {

    @Autowired
	PasswordEncoder passwordEncoder;

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
    
    List<MemberRequest> memberRequestSamples;

    MemberService memberService;

    List<Reservation> reservationSamples;

    @BeforeEach
    void beforeEach() {
        memberService = new MemberService(memberRepository, passwordEncoder);

        carSamples = carRepository.saveAll(carSamples);
        memberSamples = memberRepository.saveAll(memberSamples);
		memberRequestSamples = memberSamples.stream().map(r -> new MemberRequest(r)).collect(Collectors.toList());

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
        List<MemberResponse> responses = memberService.findAll();
        
        assertEquals(memberSamples.size(), responses.size());
    }

    @Test
    void testFind() {
        MemberResponse response = memberService.find(memberSamples.get(0).getUsername());

        assertEquals(memberSamples.get(0).getUsername(), response.getUsername());
    }

    @Test
    void testCreate() {
        memberRepository.delete(memberSamples.get(2));
        memberService.create(memberRequestSamples.get(2));

        MemberResponse response = memberService.find(memberSamples.get(2).getUsername());
        assertEquals(memberSamples.get(2).getUsername(), response.getUsername());
    }

    @Test
    void testUpdate() {
        memberRequestSamples.get(0).setCity("Houston");
        memberService.update(memberRequestSamples.get(0));

        MemberResponse response = memberService.find(memberSamples.get(0).getUsername());
        assertEquals(memberRequestSamples.get(0).getCity(), response.getCity());
    }

    @Test
    void testDelete() {
        memberService.delete(memberSamples.get(0).getUsername());

        assertThrows(ResponseStatusException.class, () -> {
			memberService.find(memberSamples.get(0).getUsername());
		});
    }

    @Test
	void testFindAllByReservationsIsNotEmpty() {
        List<MemberResponse> responses = memberService.findAllByReservationsIsNotEmpty();
        
        assertEquals(reservationSamples.size(), responses.size());
	}
}
