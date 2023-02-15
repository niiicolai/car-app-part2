package dat3.car.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dat3.car.dto.member.MemberRequest;
import dat3.car.dto.member.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
