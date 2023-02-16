package dat3.car.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dat3.car.dto.reservation.ReservationRequest;
import dat3.car.dto.reservation.ReservationResponse;
import dat3.car.service.ReservationService;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // Role: ADMIN
    @GetMapping
    public List<ReservationResponse> findAll() {
        return reservationService.findAll();
    }

    // Role: MEMBER
    @GetMapping("/{id}")
    public ReservationResponse find(@PathVariable("id") int id) {
        return reservationService.find(id);
    }

    // Role: MEMBER
    @PostMapping
    public ReservationResponse create(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.create(reservationRequest);
    }

    // Role: ADMIN
    @PatchMapping
    public ReservationResponse update(@RequestBody ReservationRequest reservationRequest) {
        return reservationService.update(reservationRequest);
    }

    // Role: ADMIN
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        reservationService.delete(id);
    }

    // Role: MEMBER
    @GetMapping("/find-all-by-member/{username}")
    public List<ReservationResponse> findAllByMember(@PathVariable("username") String username) {
        return reservationService.findAllByMember(username);
    }

    // Role: MEMBER
    @GetMapping("/count-by-member/{username}")
    public int countByMember(@PathVariable("username") String username) {
        return reservationService.countByMember(username);
    }
}
