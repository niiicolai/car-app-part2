package dat3.car.member.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dat3.car.member.dto.MemberRequest;
import dat3.car.member.dto.MemberResponse;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;

@Service
public class MemberService {

    private PasswordEncoder passwordEncoder;

    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public List<MemberResponse> findAll() {
        List<Member> members = memberRepository.findAll();
        return members.stream().map(member -> new MemberResponse(member)).collect(Collectors.toList());
    }

    public MemberResponse find(String username) {
        Optional<Member> memberOpt = memberRepository.findById(username);
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with <USERNAME> doesn't exist!");

        return new MemberResponse(memberOpt.get());
    }

    public MemberResponse create(MemberRequest memberRequest) {
        if (memberRepository.existsById(memberRequest.getUsername()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with <USERNAME> already exist!");
        Member member = memberRequest.toMember();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member = memberRepository.save(member);

        return new MemberResponse(member);
    }

    public MemberResponse update(MemberRequest memberRequest) {
        Optional<Member> memberOpt = memberRepository.findById(memberRequest.getUsername());
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with <USERNAME> doesn't exist!");
        
        Member member = memberOpt.get();
        member.setEmail(memberRequest.getEmail());
        member.setFirstName(memberRequest.getFirstName());
        member.setLastName(memberRequest.getLastName());
        member.setStreet(memberRequest.getStreet());
        member.setCity(memberRequest.getCity());
        member.setZip(memberRequest.getZip());
        member = memberRepository.save(memberRequest.toMember());
        
        return new MemberResponse(member);
    }

    public void delete(String username) {
        Optional<Member> memberOpt = memberRepository.findById(username);
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Member with <USERNAME> doesn't exist!");

        memberRepository.delete(memberOpt.get());
    }

    public List<MemberResponse> findAllByReservationsIsNotEmpty() {
        List<Member> members = memberRepository.findAllByReservationsIsNotEmpty();
        return members.stream().map(member -> new MemberResponse(member)).collect(Collectors.toList());
    }
}
