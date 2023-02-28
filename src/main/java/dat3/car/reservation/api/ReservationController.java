package dat3.car.reservation.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import dat3.car.application.dto.ErrorResponse;
import dat3.car.reservation.dto.ReservationRequest;
import dat3.car.reservation.dto.ReservationResponse;
import dat3.car.reservation.service.ReservationService;

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
    public Object create(@RequestBody ReservationRequest reservationRequest) {
        try {
            return reservationService.create(reservationRequest);
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getStatusCode(), e.getReason()));
        }
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
