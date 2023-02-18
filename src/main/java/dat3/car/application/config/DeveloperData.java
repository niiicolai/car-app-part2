package dat3.car.application.config;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.member.dto.MemberRequest;
import dat3.car.member.dto.MemberResponse;
import dat3.car.member.entity.Member;
import dat3.car.member.service.MemberService;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeveloperData implements ApplicationRunner {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    List<Reservation> reservationSamples;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (Reservation reservation : reservationSamples) {
            Member member = reservation.getMember();
            MemberRequest request = new MemberRequest(member);
            memberService.create(request);

            Car car = carRepository.save(reservation.getCar());
            reservation.setCar(car);
        }
        
        reservationRepository.saveAll(reservationSamples);
    }
}
