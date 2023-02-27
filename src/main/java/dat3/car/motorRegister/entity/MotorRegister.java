package dat3.car.motorRegister.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder 
@Getter 
@Setter 
@Entity
public class MotorRegister {
    
    @Id
    private String registrationNumber;
    
    private String status; 
    
    private String statusDate;
    
    private String type;
    
    // Note: A column cannot be named 'use' in MySQL.
    @Column(name = "purpose")
    private String use;
    
    private String firstRegistration;
    
    private String vin;
    
    private int doors;
    
    private String make;
    
    private String model;
    
    private String variant;
    
    private String modelType;
    
    private String color;
    
    private String fuelType;
    
    private boolean isLeasing;
    
    private String leasingFrom;
    
    private String leasingTo;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime lastEdited;
}
