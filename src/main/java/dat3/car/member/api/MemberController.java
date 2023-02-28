package dat3.car.member.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dat3.car.application.dto.ErrorResponse;
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

    // Role: MEMBER
    @GetMapping("/{username}")
    public Object find(@PathVariable("username") String username) {
        try {
            return memberService.find(username);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }    

    // Role: ANONYMOUS
    @PostMapping
    public Object create(@RequestBody MemberRequest memberRequest) {
        try {
            return memberService.create(memberRequest);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: MEMBER
    @PatchMapping
    public Object update(@RequestBody MemberRequest memberRequest) {
        try {
            return memberService.update(memberRequest);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: ADMIN
    @DeleteMapping("/{username}")
    public Object delete(@PathVariable("username") String username) {
        try {
            memberService.delete(username);
            return ResponseEntity.ok();
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: ADMIN
    @GetMapping("/no-reservations")
    public List<MemberResponse> findAllByReservationsIsNotEmpty() {
        return memberService.findAllByReservationsIsNotEmpty();
    }
}
