package dat3.car.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationResponse {

    @JsonProperty
    private int id;

    @JsonProperty
    private int carId;

    @JsonProperty
    private String carBrand;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private LocalDateTime rentalDate;
    
    public ReservationResponse(Reservation reservation) {
        id = reservation.getId();
        carId = reservation.getCar().getId();
        carBrand = reservation.getCar().getBrand();
        rentalDate = reservation.getRentalDate();
    }
}
