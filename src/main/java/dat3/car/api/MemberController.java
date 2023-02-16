package dat3.car.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dat3.car.dto.member.MemberRequest;
import dat3.car.dto.member.MemberResponse;
import dat3.car.service.MemberService;

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
}
