package dat3.car.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonProperty
    private LocalDateTime created;

    @JsonProperty
    private LocalDateTime lastEdited;

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
    }
}
