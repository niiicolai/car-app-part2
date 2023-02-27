package dat3.car.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dat3.car.car.entity.Car;
import dat3.car.car.repository.CarRepository;
import dat3.car.member.entity.Member;
import dat3.car.member.repository.MemberRepository;
import dat3.car.reservation.dto.ReservationRequest;
import dat3.car.reservation.dto.ReservationResponse;
import dat3.car.reservation.entity.Reservation;
import dat3.car.reservation.repository.ReservationRepository;

@Service
public class ReservationService {

    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    private CarRepository carRepository;

    public ReservationService(
        ReservationRepository reservationRepository,
        MemberRepository memberRepository,
        CarRepository carRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.carRepository = carRepository;
    }

    public List<ReservationResponse> findAll() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream().map(reservation -> new ReservationResponse(reservation)).collect(Collectors.toList());
    }

    public ReservationResponse find(int id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation with <ID> doesn't exist!");

        return new ReservationResponse(reservationOpt.get());
    }
    
    public ReservationResponse create(ReservationRequest reservationRequest) {
        Optional<Car> carOpt = carRepository.findById(reservationRequest.getCarId());
        if (carOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car with <id> doesn't exist!");

        Optional<Member> memberOpt = memberRepository.findById(reservationRequest.getMemberUsername());
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member with <USERNAME> doesn't exist!");

        LocalDateTime rentalDate = reservationRequest.getRentalDate().atStartOfDay();
        if (rentalDate.isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The rental date cannot be in the past!");

        if (reservationRepository.existsByCarAndRentalDate(carOpt.get(), rentalDate))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already has a reservation at the given date!");

        Reservation reservation = new Reservation(memberOpt.get(), carOpt.get(), rentalDate);
        reservation = reservationRepository.save(reservation);
        
        return new ReservationResponse(reservation);
    }

    public ReservationResponse update(ReservationRequest reservationRequest) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationRequest.getId());
        if (reservationOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation with <ID> doesn't exist!");

        LocalDateTime rentalDate = reservationRequest.getRentalDate().atStartOfDay();
        if (rentalDate.isBefore(LocalDateTime.now()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The rental date cannot be in the past!");
    
        Car car = reservationOpt.get().getCar();
        if (reservationRepository.existsByCarAndRentalDate(car, rentalDate))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car already has a reservation at the given date!");

        Reservation reservation = reservationOpt.get();
        reservation.setRentalDate(reservationRequest.getRentalDate().atStartOfDay());
        reservation = reservationRepository.save(reservation);

        return new ReservationResponse(reservation);
    }

    public void delete(int id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation with <ID> doesn't exist!");

        reservationRepository.delete(reservationOpt.get());
    }

    public List<ReservationResponse> findAllByMember(String username) {
        Optional<Member> memberOpt = memberRepository.findById(username);
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member with <USERNAME> doesn't exist!");

        List<Reservation> reservations = reservationRepository.findAllByMember(memberOpt.get());
        return reservations.stream().map(reservation -> new ReservationResponse(reservation)).collect(Collectors.toList());
    }

    public int countByMember(String username) {
        Optional<Member> memberOpt = memberRepository.findById(username);
        if (memberOpt.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member with <USERNAME> doesn't exist!");

        return reservationRepository.countByMember(memberOpt.get());
    }
}
