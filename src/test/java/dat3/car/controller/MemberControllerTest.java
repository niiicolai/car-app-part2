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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.context.annotation.Import;

import com.fasterxml.jackson.databind.ObjectMapper;

import dat3.car.config.ObjectMapperConfig;
import dat3.car.config.SampleTestConfig;
import dat3.car.config.SecurityTestConfig;
import dat3.car.member.api.MemberController;
import dat3.car.member.dto.MemberRequest;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.member.service.MemberService;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import({SampleTestConfig.class, ObjectMapperConfig.class, SecurityTestConfig.class})
public class MemberControllerTest {
    
    @Autowired
	MemberRepository memberRepository;

	@Autowired 
    List<Member> memberSamples;

	@Autowired 
    List<MemberRequest> memberRequestSamples;

	@Autowired 
	ObjectMapper objectMapper;

	@Autowired
	PasswordEncoder passwordEncoder;

    MockMvc mockMvc;

	@BeforeAll
	void beforeAll() {
		MemberService memberService = new MemberService(memberRepository, passwordEncoder);
        MemberController memberController = new MemberController(memberService);
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();

        memberRepository.save(memberSamples.get(0));
        memberRepository.save(memberSamples.get(1));
	}

    @AfterAll
    void afterAll() {
        memberRepository.deleteAll();
    }
    
	@Test
	void testFindAll() throws Exception {
        mockMvc.perform(get("/api/v1/members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
				.andExpect(jsonPath("$.[0].username", is(memberSamples.get(0).getUsername())));
	}

    @Test
	void testFind() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/members/%s", memberSamples.get(0).getUsername())))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(memberSamples.get(0).getUsername())));
	}

	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/api/v1/members")
					.content(objectMapper.writeValueAsString(memberRequestSamples.get(2)))
	                .contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.username", is(memberSamples.get(2).getUsername())));
	}

	@Test
	void testUpdate() throws Exception {
		memberRequestSamples.get(0).setCity("A new city");

		mockMvc.perform(patch("/api/v1/members")
					.content(objectMapper.writeValueAsString(memberRequestSamples.get(0)))
					.contentType(MediaType.APPLICATION_JSON)
					.characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
				.andExpect(jsonPath("$.city", is(memberRequestSamples.get(0).getCity())))
				.andExpect(jsonPath("$.username", is(memberRequestSamples.get(0).getUsername())));
	}

	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete(String.format("/api/v1/members/%s", memberSamples.get(0).getUsername())))
                .andDo(print())
                .andExpect(status().isOk());
	}

	@Test
	void testFindAllByReservationsIsNotEmpty() throws Exception {
		// Ideal: Add an reservation to one of the members.
        mockMvc.perform(get("/api/v1/members/no-reservations"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(0)));
	}
}
