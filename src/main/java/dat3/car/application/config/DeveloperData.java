package dat3.car.application.config;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.member.dto.MemberRequest;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.member.service.MemberService;
import dat3.car.motorRegister.entity.MotorRegister;
import dat3.car.motorRegister.repository.MotorRegisterRepository;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

import java.time.LocalDateTime;
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
    private MemberService memberService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MotorRegisterRepository motorRegisterRepository;

    @Autowired
    private List<Member> memberSamples;

    @Autowired
    private List<Car> carSamples;

    @Autowired
    private List<MotorRegister> motorRegisterSamples;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        carSamples = carRepository.saveAll(carSamples);

        // Use member service to ensure password is encrypted.
        for (int i = 0; i < memberSamples.size(); i++) {
            MemberRequest request = new MemberRequest(memberSamples.get(i));
            memberService.create(request);
            memberSamples.set(i, memberRepository.findById(request.getUsername()).get());
        }

        reservationRepository.save(new Reservation(memberSamples.get(0), carSamples.get(0), LocalDateTime.now()));
        reservationRepository.save(new Reservation(memberSamples.get(1), carSamples.get(1), LocalDateTime.now()));
        
        motorRegisterRepository.saveAll(motorRegisterSamples);
    }
}
