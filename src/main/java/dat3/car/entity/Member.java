package dat3.car.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor
@Getter 
@Setter 
public class Member {

  @Id
  private String username;

  private String password;
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

  @CreationTimestamp
  private LocalDateTime created;

  @UpdateTimestamp
  private LocalDateTime lastEdited;

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
