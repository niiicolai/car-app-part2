package dat3.car.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequest {
    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

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

    public MemberRequest(Member member) {
        username = member.getUsername();
        password = member.getPassword();
        email = member.getEmail();
        firstName = member.getFirstName();
        lastName = member.getLastName();
        street = member.getStreet();
        city = member.getCity();
        zip = member.getZip();
    }

    public Member toMember() {
        return new Member(username, password, email, firstName, lastName, street, city, zip);
    }
}
