package dat3.car.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import dat3.car.config.ObjectMapperTestConfig;
import dat3.car.config.SampleTestConfig;
import dat3.car.car.api.CarController;
import dat3.car.car.dto.CarRequest;
import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.car.service.CarService;

@DataJpaTest
@Import({SampleTestConfig.class, ObjectMapperTestConfig.class})
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

	@BeforeEach
	void beforeEach() {
		CarService carService = new CarService(carRepository);
        CarController carController = new CarController(carService);
        mockMvc = MockMvcBuilders.standaloneSetup(carController).build();

		// Ensure number two sample has the highest discount
		carSamples.get(1).setBestDiscount(99999999);

        carSamples.get(0).setId(carRepository.save(carSamples.get(0)).getId());
        carSamples.get(1).setId(carRepository.save(carSamples.get(1)).getId());
	}

    @AfterEach
    void afterEach() {
        carRepository.deleteAll();
    }
    
	@Test
	void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/cars"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].make", is(carSamples.get(0).getMake())));
	}

    @Test
	void testFind() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/cars/%s", carSamples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.make", is(carSamples.get(0).getMake())));
	}

	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/api/v1/cars")
					.content(objectMapper.writeValueAsString(carRequestSamples.get(2)))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.make", is(carSamples.get(2).getMake())));
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

	/*
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/cars/%s", carSamples.get(0).getId())))
                .andDo(print())
                .andExpect(status().isOk());
	}
	*/

	@Test
	void testFindAllByBrandAndModel() throws Exception {
		String url = String.format("/api/v1/cars/by/%s/%s", carSamples.get(0).getMake(), carSamples.get(0).getModel());
		mockMvc.perform(get(url))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
				.andExpect(jsonPath("$.[0].make", is(carSamples.get(0).getMake())))
				.andExpect(jsonPath("$.[0].model", is(carSamples.get(0).getModel())));
	}

	@Test
	void testFindAveragePricePrDay() throws Exception {
		double expectedAverage = 0;
        expectedAverage += carSamples.get(0).getPricePrDay();
		expectedAverage += carSamples.get(1).getPricePrDay();
        expectedAverage /= 2;
		mockMvc.perform(get("/api/v1/cars/average-price-pr-day"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(expectedAverage)));
	}

	@Test
	void testFindAllWithBestDiscount() throws Exception {
		mockMvc.perform(get("/api/v1/cars/best-discount"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
				.andExpect(jsonPath("$.[0].make", is(carSamples.get(1).getMake())))
				.andExpect(jsonPath("$.[0].model", is(carSamples.get(1).getModel())));
	}

	@Test
	void testFindAllByReservationsIsEmpty() throws Exception {
		// Ideal: Add an reservation to one of the cars.
		mockMvc.perform(get("/api/v1/cars/no-reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].make", is(carSamples.get(0).getMake())))
				.andExpect(jsonPath("$.[0].model", is(carSamples.get(0).getModel())));
	}
}
