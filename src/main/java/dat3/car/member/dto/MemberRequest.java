package dat3.car.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import dat3.car.member.entity.Member;
import dat3.car.security.dto.UserWithRolesRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequest extends UserWithRolesRequest {

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
        super(member);
        email = member.getEmail();
        firstName = member.getFirstName();
        lastName = member.getLastName();
        street = member.getStreet();
        city = member.getCity();
        zip = member.getZip();
    }

    public Member toMember() {
        Member member = new Member(getUsername(), getPassword(), email, firstName, lastName, street, city, zip);
        member.setAccountNonExpired(isAccountNonExpired());
        member.setAccountNonLocked(isAccountNonLocked());
        member.setEnabled(isEnabled());
        member.setCredentialsNonExpired(isCredentialsNonExpired());
        member.setRoles(getRoles());
        return member;
    }
}
