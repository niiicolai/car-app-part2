package dat3.car.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dat3.security.entity.UserWithRoles;

@Entity
@NoArgsConstructor
@Getter 
@Setter 
public class Member extends UserWithRoles {

  private String email;
  private String firstName;
  private String lastName;
  private String street;
  private String city;
  private String zip;

  private boolean approved;

  private int ranking;

  @ElementCollection
  private List<String> favoriteCarColors = new ArrayList<>();

  @ElementCollection
  @MapKeyColumn(name = "Description")
  @Column(name = "phoneNumber")
  private Map<String, String> phones = new HashMap<>();

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Reservation> reservations;

  public Member(
    String user,
    String password,
    String email,
    String firstName,
    String lastName,
    String street,
    String city,
    String zip
  ) {
    this.username = user;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.street = street;
    this.city = city;
    this.zip = zip;
  }
}
