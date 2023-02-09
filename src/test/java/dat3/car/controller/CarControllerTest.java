package dat3.car.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.api.CarController;
import dat3.car.dto.CarRequest;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;
import dat3.car.service.CarService;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class CarControllerTest {
    
    @Autowired
	CarRepository repository;

	CarService service;

    CarController controller;

    List<CarRequest> sampleRequests;

	List<Car> samples;

    MockMvc mockMvc;

	@BeforeAll
	void beforeAll() {
		service = new CarService(repository);
        controller = new CarController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		samples = service.sampleCars();
        sampleRequests = service.sampleRequests();

        samples.get(0).setId(repository.save(samples.get(0)).getId());
        samples.get(1).setId(repository.save(samples.get(1)).getId());
	}

    @AfterAll
    void afterAll() {
        repository.deleteAll();
    }
    
	@Test
	void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].brand", is(samples.get(0).getBrand())));
	}

    @Test
	void testFind() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/cars/%s", samples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.brand", is(samples.get(0).getBrand())));
	}

	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/api/v1/cars")
					.content(new ObjectMapper().writeValueAsString(sampleRequests.get(2)))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.brand", is(samples.get(2).getBrand())));
	}

	@Test
	void testUpdate() throws Exception {
		sampleRequests.get(0).setBestDiscount(99999);

		mockMvc.perform(patch("/api/v1/cars")
					.content(new ObjectMapper().writeValueAsString(sampleRequests.get(0)))
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.bestDiscount", is(sampleRequests.get(0).getBestDiscount())));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/cars/%s", samples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk());
	}
}
