package dat3.car.api;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import dat3.car.dto.ReservationRequest;
import dat3.car.dto.ReservationResponse;
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

    // Role: ADMIN
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
}
