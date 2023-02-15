package dat3.car.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @JsonProperty
    private String memberUsername;

    @JsonProperty
    private int carId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private LocalDateTime rentalDate;

    public ReservationRequest(Reservation reservation) {
        memberUsername = reservation.getMember().getUsername();
        carId = reservation.getCar().getId();
        rentalDate = reservation.getRentalDate();
    }
}
