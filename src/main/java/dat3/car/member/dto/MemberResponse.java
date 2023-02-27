package dat3.car.member.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.member.entity.Member;
import dat3.car.reservation.dto.ReservationResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberResponse {

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty
    private String firstName;

    @JsonProperty
    private String lastName;

    @JsonProperty
    private String street;

    @JsonProperty
    private String city;

    @JsonProperty
    private String zip;

    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd") 
    private LocalDateTime lastEdited;

    @JsonProperty
    private List<ReservationResponse> reservations;

    public MemberResponse(Member member) {
        username = member.getUsername();
        email = member.getEmail();
        firstName = member.getFirstName();
        lastName = member.getLastName();
        street = member.getStreet();
        city = member.getCity();
        zip = member.getZip();
        created = member.getCreated();
        lastEdited = member.getLastEdited();

        if (member.getReservations() != null &&
            member.getReservations().size() > 0)
            reservations = member.getReservations()
                .stream()
                .map(r -> ReservationResponse.builder()
                    .id(r.getId())
                    .carId(r.getCar().getId())
                    .carMake(r.getCar().getMake())
                    .rentalDate(r.getRentalDate())
                    .build())
                    .collect(Collectors.toList());
                                
    }
}
