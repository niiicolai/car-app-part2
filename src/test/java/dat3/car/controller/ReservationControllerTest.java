package dat3.car.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.api.ReservationController;
import dat3.car.config.ObjectMapperConfig;
import dat3.car.config.SampleTestConfig;
import dat3.car.dto.reservation.ReservationRequest;
import dat3.car.entity.Reservation;
import dat3.car.entity.Member;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;
import dat3.car.service.ReservationService;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import({SampleTestConfig.class, ObjectMapperConfig.class})
public class ReservationControllerTest {
    
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

	@Autowired 
	ObjectMapper objectMapper;

    MockMvc mockMvc;

	@BeforeAll
	void beforeAll() {
		ReservationService reservationService = new ReservationService(reservationRepository, 
			memberRepository, carRepository);
        ReservationController reservationController = new ReservationController(reservationService);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

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
		mockMvc.perform(post("/api/v1/reservations")
					.content(objectMapper.writeValueAsString(reservationRequestSamples.get(2)))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.carBrand", is(reservationSamples.get(2).getCar().getBrand())));
	}

	@Test
	void testUpdate() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime dateTime = LocalDateTime.parse("2012-12-12 10:10:10.1", formatter);
		reservationRequestSamples.get(0).setRentalDate(dateTime);
		reservationRequestSamples.get(0).setId(reservationSamples.get(0).getId());

		mockMvc.perform(patch("/api/v1/reservations")
					.content(objectMapper.writeValueAsString(reservationRequestSamples.get(0)))
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.rentalDate", is(reservationRequestSamples.get(0).getRentalDate().format(formatter))))
				.andExpect(jsonPath("$.id", is(reservationRequestSamples.get(0).getId())));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/reservations/%s", reservationSamples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk());
	}
}
