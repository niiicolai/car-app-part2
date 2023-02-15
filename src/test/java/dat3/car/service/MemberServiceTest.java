package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import dat3.car.config.SampleTestConfig;
import dat3.car.dto.member.MemberRequest;
import dat3.car.dto.member.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Import(SampleTestConfig.class)
public class MemberServiceTest {

    @Autowired 
    MemberRepository memberRepository;

    @Autowired 
    List<Member> memberSamples;
    
    @Autowired 
    List<MemberRequest> memberRequestSamples;

    MemberService memberService;

    @BeforeAll
    void beforeAll() {
        memberService = new MemberService(memberRepository);

        memberRepository.save(memberSamples.get(0));
        memberRepository.save(memberSamples.get(1));
    }

    @AfterAll
    void afterAll() {
        memberRepository.deleteAll();
    }
    
    @Test
    void testFindAll() {
        List<MemberResponse> responses = memberService.findAll();
        
        assertEquals(2, responses.size());
    }

    @Test
    void testFind() {
        MemberResponse response = memberService.find(memberSamples.get(0).getUsername());

        assertEquals(memberSamples.get(0).getUsername(), response.getUsername());
    }

    @Test
    void testCreate() {
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
}
