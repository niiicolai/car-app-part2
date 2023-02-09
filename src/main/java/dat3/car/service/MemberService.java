package dat3.car.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> sampleMembers() {
        return Arrays.asList(new Member[] {
            new Member("user1", "pass1", "user1@email.com", "John", "Doe", "123 Main St", "New York", "10001"),
            new Member("user2", "pass2", "user2@email.com", "Jane", "Smith", "456 Elm St", "Los Angeles", "90001"),
            new Member("user3", "pass3", "user3@email.com", "Bob", "Johnson", "789 Oak St", "Chicago", "60601"),
            new Member("user4", "pass4", "user4@email.com", "Emily", "Brown", "246 Pine St", "Houston", "77001"),
            new Member("user5", "pass5", "user5@email.com", "Michael", "Davis", "369 Cedar St", "Phoenix", "85001")
        });
    }

    public List<MemberRequest> sampleRequests() {
        return sampleMembers().stream().map(member -> new MemberRequest(member)).collect(Collectors.toList());
    }
    
    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(member -> new MemberResponse(member)).collect(Collectors.toList());
    }

    public MemberResponse find(String username) {
        Optional<Member> memberOpt = memberRepository.findById(username);

        return new MemberResponse(memberOpt.get());
    }

    public MemberResponse create(MemberRequest memberRequest) {
        Member member = memberRepository.save(memberRequest.toMember());
        return new MemberResponse(member);
    }

    public MemberResponse update(MemberRequest memberRequest) {
        Member member = memberRepository.save(memberRequest.toMember());
        return new MemberResponse(member);
    }

    public void delete(String username) {
        memberRepository.deleteById(username);
    }
}
