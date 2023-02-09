package dat3.car.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.api.MemberController;
import dat3.car.dto.MemberRequest;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;
import dat3.car.service.MemberService;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class MemberControllerTest {
    
    @Autowired
	MemberRepository repository;

	MemberService service;

    MemberController controller;

    List<MemberRequest> sampleRequests;

	List<Member> samples;

    MockMvc mockMvc;

	@BeforeAll
	void beforeAll() {
		service = new MemberService(repository);
        controller = new MemberController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		samples = service.sampleMembers();
        sampleRequests = service.sampleRequests();

        repository.save(samples.get(0));
        repository.save(samples.get(1));
	}

    @AfterAll
    void afterAll() {
        repository.deleteAll();
    }
    
	@Test
	void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].username", is(samples.get(0).getUsername())));
	}

    @Test
	void testFind() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/members/%s", samples.get(0).getUsername())))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(samples.get(0).getUsername())));
	}

	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/api/v1/members")
					.content(new ObjectMapper().writeValueAsString(sampleRequests.get(2)))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(samples.get(2).getUsername())));
	}

	@Test
	void testUpdate() throws Exception {
		sampleRequests.get(0).setCity("A new city");

		mockMvc.perform(patch("/api/v1/members")
					.content(new ObjectMapper().writeValueAsString(sampleRequests.get(0)))
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.city", is(sampleRequests.get(0).getCity())));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/members/%s", samples.get(0).getUsername())))
                .andDo(print())
                .andExpect(status().isOk());
	}
}
