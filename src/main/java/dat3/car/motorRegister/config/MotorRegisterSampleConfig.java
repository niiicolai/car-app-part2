package dat3.car.motorRegister.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dat3.car.motorRegister.entity.MotorRegister;

@Configuration
public class MotorRegisterSampleConfig {

    @Bean
    public List<MotorRegister> motorRegisterSamples() {
        return Arrays.asList(new MotorRegister[] {
            MotorRegister.builder()
                .registrationNumber("JN79863")
                .status("Registreret")
                .statusDate("2022-11-25T13:25:50.000+01:00")
                .type("Personbil")
                .use("Privat personkørsel")
                .firstRegistration("2015-05-26+02:00")
                .vin("XBCYYW8K1DP684932")
                .doors(4)
                .make("Toyota")
                .model("Camry")
                .variant("3.0 TDI 272 HK 4-DØRS QUATTRO S tronic")
                .modelType("4G")
                .color("Sort")
                .fuelType("Diesel")
                .isLeasing(false)
                .leasingFrom("2018-12-21+01:00")
                .leasingTo("2022-11-21+01:00")
                .build()
        });
    }
}