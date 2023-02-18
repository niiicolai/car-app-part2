package dat3.car.member.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dat3.car.member.dto.MemberRequest;
import dat3.car.member.dto.MemberResponse;
import dat3.car.member.service.MemberService;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Role: ADMIN
    @GetMapping
    public List<MemberResponse> findAll() {
        return memberService.findAll();
    }

    // Role: USER
    @GetMapping("/{username}")
    public MemberResponse find(@PathVariable("username") String username) {
        return memberService.find(username);
    }

    // Role: ANONYMOUS
    @PostMapping
    public MemberResponse create(@RequestBody MemberRequest memberRequest) {
        return memberService.create(memberRequest);
    }

    // Role: USER
    @PatchMapping
    public MemberResponse update(@RequestBody MemberRequest memberRequest) {
        return memberService.update(memberRequest);
    }

    // Role: USER
    @DeleteMapping("/{username}")
    public void delete(@PathVariable("username") String username) {
        memberService.delete(username);
    }

    // Role: ADMIN
    @GetMapping("/no-reservations")
    public List<MemberResponse> findAllByReservationsIsNotEmpty() {
        return memberService.findAllByReservationsIsNotEmpty();
    }
}
