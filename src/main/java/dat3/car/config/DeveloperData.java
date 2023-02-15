package dat3.car.config;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import dat3.car.repository.CarRepository;
import dat3.car.repository.MemberRepository;
import dat3.car.repository.ReservationRepository;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeveloperData implements ApplicationRunner {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    List<Member> memberSamples;

    @Autowired
    List<Car> carSamples;

    @Autowired
    List<Reservation> reservationSamples;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberRepository.saveAll(memberSamples);
        carRepository.saveAll(carSamples);
        reservationRepository.saveAll(reservationSamples);
    }
}
