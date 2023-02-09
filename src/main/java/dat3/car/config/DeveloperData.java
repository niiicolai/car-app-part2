package dat3.car.config;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeveloperData implements ApplicationRunner {

  private MemberRepository memberRepository;
  private CarRepository carRepository;

  public DeveloperData(
    MemberRepository memberRepository,
    CarRepository carRepository
  ) {
    this.memberRepository = memberRepository;
    this.carRepository = carRepository;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    
    // Add Test members
    memberRepository.save(new Member("user1", "pass1", "user1@email.com", "John", "Doe", "123 Main St", "New York", "10001"));
    memberRepository.save(new Member("user2", "pass2", "user2@email.com", "Jane", "Smith", "456 Elm St", "Los Angeles", "90001"));
    memberRepository.save(new Member("user3", "pass3", "user3@email.com", "Bob", "Johnson", "789 Oak St", "Chicago", "60601"));
    memberRepository.save(new Member("user4", "pass4", "user4@email.com", "Emily", "Brown", "246 Pine St", "Houston", "77001"));
    memberRepository.save(new Member("user5", "pass5", "user5@email.com", "Michael", "Davis", "369 Cedar St", "Phoenix", "85001"));

    // Test colors
    String[] favoriteCarColors = {"green", "red", "blue"};

    // Test phone numbers
    Map<String,String> phones = new HashMap<>();
    phones.put("work", "555-555-5555");
    phones.put("home", "555-555-1234");
    phones.put("mobile", "555-555-4321");

    // Update colors and phone numbers
    Iterable<Member> members = memberRepository.findAll();

    for (Member member : members) {
        member.setFavoriteCarColors(Arrays.asList(favoriteCarColors));
        member.setPhones(phones);
    }

    memberRepository.saveAll(members);

    // Add Test cars
    carRepository.save(new Car("Toyota", "Camry", 150, 40000));
    carRepository.save(new Car("Honda", "Civic", 200, 35000));
    carRepository.save(new Car("Tesla", "Model 3", 350, 55000));
    carRepository.save(new Car("Ford", "Mustang", 580, 32000));
    carRepository.save(new Car("Chevrolet", "Corvette", 480, 75000));
  }
}
