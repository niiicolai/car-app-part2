package dat3.car.motorRegister.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dat3.car.motorRegister.entity.MotorRegister;

public interface MotorRegisterRepository extends JpaRepository<MotorRegister, String> {
}
