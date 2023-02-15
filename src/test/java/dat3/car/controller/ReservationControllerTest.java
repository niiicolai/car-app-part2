package dat3.car.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.api.ReservationController;
import dat3.car.config.DeveloperData;
import dat3.car.dto.ReservationRequest;
import dat3.car.entity.Reservation;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;
import dat3.car.service.ReservationService;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class ReservationControllerTest {
    
    @Autowired
	ReservationRepository reservationRepository;

    @Autowired
	MemberRepository memberRepository;

    @Autowired
	CarRepository carRepository;

    @Autowired
    DeveloperData developerData;

	ReservationService service;

    ReservationController controller;

    List<ReservationRequest> sampleRequests;

	List<Reservation> samples;

    MockMvc mockMvc;

	@BeforeAll
	void beforeAll() {
		service = new ReservationService(reservationRepository, memberRepository, carRepository);
        controller = new ReservationController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        samples = developerData.reservationSamples();
        sampleRequests = samples.stream().map(r -> new ReservationRequest(r)).collect(Collectors.toList());

        reservationRepository.save(samples.get(0));
        reservationRepository.save(samples.get(1));
	}

    @AfterAll
    void afterAll() {
        reservationRepository.deleteAll();
    }
    
	@Test
	void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].carBrand", is(samples.get(0).getCar().getBrand())));
	}

    @Test
	void testFind() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/reservations/%s", samples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.carBrand", is(samples.get(0).getCar().getBrand())));
	}

	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/api/v1/reservations")
					.content(new ObjectMapper().writeValueAsString(sampleRequests.get(2)))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.carBrand", is(samples.get(2).getCar().getBrand())));
	}

	@Test
	void testUpdate() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse("2012-12-12 10:10:10.10", formatter);
		sampleRequests.get(0).setRentalDate(dateTime);

		mockMvc.perform(patch("/api/v1/reservations")
					.content(new ObjectMapper().writeValueAsString(sampleRequests.get(0)))
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.rentalDate", is(sampleRequests.get(0).getRentalDate())));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/reservations/%s", samples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk());
	}

}
