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
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.config.ObjectMapperConfig;
import dat3.car.config.SampleTestConfig;
import dat3.car.dto.car.CarRequest;
import dat3.car.api.CarController;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;
import dat3.car.service.CarService;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import({SampleTestConfig.class, ObjectMapperConfig.class})
public class CarControllerTest {    

    @Autowired
	CarRepository carRepository;	

    @Autowired 
    List<Car> carSamples;
	
	@Autowired 
	List<CarRequest> carRequestSamples;

	@Autowired 
	ObjectMapper objectMapper;

    MockMvc mockMvc;

	@BeforeAll
	void beforeAll() {
		CarService carService = new CarService(carRepository);
        CarController carController = new CarController(carService);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

        carSamples.get(0).setId(carRepository.save(carSamples.get(0)).getId());
        carSamples.get(1).setId(carRepository.save(carSamples.get(1)).getId());
	}

    @AfterAll
    void afterAll() {
        carRepository.deleteAll();
    }
    
	@Test
	void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].brand", is(carSamples.get(0).getBrand())));
	}

    @Test
	void testFind() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/cars/%s", carSamples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.brand", is(carSamples.get(0).getBrand())));
	}

	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/api/v1/cars")
					.content(objectMapper.writeValueAsString(carRequestSamples.get(2)))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.brand", is(carSamples.get(2).getBrand())));
	}

	@Test
	void testUpdate() throws Exception {
		carRequestSamples.get(0).setId(carSamples.get(0).getId());
		carRequestSamples.get(0).setBestDiscount(99999);

		mockMvc.perform(patch("/api/v1/cars")
					.content(objectMapper.writeValueAsString(carRequestSamples.get(0)))
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.bestDiscount", is(carRequestSamples.get(0).getBestDiscount())))
				.andExpect(jsonPath("$.id", is(carRequestSamples.get(0).getId())));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/cars/%s", carSamples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk());
	}
}
