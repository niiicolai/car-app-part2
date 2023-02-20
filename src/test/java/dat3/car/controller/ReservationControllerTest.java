package dat3.car.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.config.ObjectMapperConfig;
import dat3.car.config.SampleTestConfig;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.reservation.api.ReservationController;
import dat3.car.reservation.dto.ReservationRequest;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;
import dat3.car.reservation.service.ReservationService;

@DataJpaTest
@Import({SampleTestConfig.class, ObjectMapperConfig.class})
public class ReservationControllerTest {
    
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

	@Autowired 
	ObjectMapper objectMapper;

    MockMvc mockMvc;

	@BeforeEach
	void beforeEach() {
		ReservationService reservationService = new ReservationService(reservationRepository, 
			memberRepository, carRepository);
        ReservationController reservationController = new ReservationController(reservationService);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

		carSamples = carRepository.saveAll(carSamples);
        memberSamples = memberRepository.saveAll(memberSamples);

        reservationSamples = new ArrayList<Reservation>(Arrays.asList(
            reservationRepository.save(new Reservation(memberSamples.get(0), carSamples.get(0), LocalDateTime.now())),
			reservationRepository.save(new Reservation(memberSamples.get(1), carSamples.get(1), LocalDateTime.now()))
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
	void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].carBrand", is(reservationSamples.get(0).getCar().getBrand())));
	}

    @Test
	void testFind() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/reservations/%s", reservationSamples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.carBrand", is(reservationSamples.get(0).getCar().getBrand())));
	}

	@Test
	void testCreate() throws Exception {
		ReservationRequest request = new ReservationRequest(
			new Reservation(memberSamples.get(2), carSamples.get(2), LocalDateTime.now().plusDays(1)));

		mockMvc.perform(post("/api/v1/reservations")
					.content(objectMapper.writeValueAsString(request))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.carBrand", is(carSamples.get(2).getBrand())));
	}

	@Test
	void testUpdate() throws Exception {
        LocalDate dateTime = LocalDate.now().plusDays(1);
		reservationRequestSamples.get(0).setRentalDate(dateTime);
		reservationRequestSamples.get(0).setId(reservationSamples.get(0).getId());

		mockMvc.perform(patch("/api/v1/reservations")
					.content(objectMapper.writeValueAsString(reservationRequestSamples.get(0)))
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.rentalDate", is(dateTime.toString())))
				.andExpect(jsonPath("$.id", is(reservationRequestSamples.get(0).getId())));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/reservations/%s", reservationSamples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk());
	}

	@Test
	void findAllByMember() throws Exception {
		String url = String.format("/api/v1/reservations/find-all-by-member/%s", 
			reservationSamples.get(0).getMember().getUsername());

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
				.andExpect(jsonPath("$.[0].carBrand", is(reservationSamples.get(0).getCar().getBrand())));
	}

	@Test
	void countByMember() throws Exception {
		String url = String.format("/api/v1/reservations/count-by-member/%s", 
			reservationSamples.get(0).getMember().getUsername());

        mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
	}
}
