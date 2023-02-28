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
    public Object find(@PathVariable("id") int id) {
        try {
            return reservationService.find(id);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: MEMBER
    @PostMapping
    public Object create(@RequestBody ReservationRequest reservationRequest) {
        try {
            return reservationService.create(reservationRequest);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: ADMIN
    @PatchMapping
    public Object update(@RequestBody ReservationRequest reservationRequest) {
        try {
            return reservationService.update(reservationRequest);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: ADMIN
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable("id") int id) {
        try {
            reservationService.delete(id);
            return ResponseEntity.ok();
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: MEMBER
    @GetMapping("/find-all-by-member/{username}")
    public Object findAllByMember(@PathVariable("username") String username) {
        try {
            return reservationService.findAllByMember(username);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }

    // Role: MEMBER
    @GetMapping("/count-by-member/{username}")
    public Object countByMember(@PathVariable("username") String username) {
        try {
            return reservationService.countByMember(username);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getReason()), e.getStatusCode());
        }
    }
}
