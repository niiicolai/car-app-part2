package dat3.car.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
public class MemberServiceTest {

    @Autowired 
    MemberRepository repository;

    MemberService service;

    List<Member> samples;
    
    List<MemberRequest> sampleRequests;

    @BeforeAll
    void beforeAll() {
        service = new MemberService(repository);
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
    void testFindAll() {
        List<MemberResponse> responses = service.findAll();
        
        assertEquals(2, responses.size());
    }

    @Test
    void testFind() {
        MemberResponse response = service.find(samples.get(0).getUsername());

        assertEquals(samples.get(0).getUsername(), response.getUsername());
    }

    @Test
    void testCreate() {
        service.create(sampleRequests.get(2));

        MemberResponse response = service.find(samples.get(2).getUsername());
        assertEquals(samples.get(2).getUsername(), response.getUsername());
    }

    @Test
    void testUpdate() {
        sampleRequests.get(0).setCity("Houston");
        service.update(sampleRequests.get(0));

        MemberResponse response = service.find(samples.get(0).getUsername());
        assertEquals(sampleRequests.get(0).getCity(), response.getCity());
    }

    @Test
    void testDelete() {
        service.delete(samples.get(0).getUsername());

        assertThrows(NoSuchElementException.class, () -> {
			service.find(samples.get(0).getUsername());
		});
    }
}
