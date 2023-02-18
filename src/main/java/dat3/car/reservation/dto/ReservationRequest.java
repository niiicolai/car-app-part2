package dat3.car.reservation.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.reservation.entity.Reservation;
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
    private int id;

    @JsonProperty
    private String memberUsername;

    @JsonProperty
    private int carId;
    
    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDate rentalDate;

    public ReservationRequest(Reservation reservation) {
        id = reservation.getId();
        memberUsername = reservation.getMember().getUsername();
        carId = reservation.getCar().getId();
        rentalDate = reservation.getRentalDate().toLocalDate();
    }
}
