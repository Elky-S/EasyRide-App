package com.example.travle.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * מודל המייצג אוטובוס בצי הרכב.
 */
@Entity
@Table(name = "buses")
@Data
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // קוד פנימי של האוטובוס (Primary Key)

    private String licensePlate; // מספר לוחית רישוי
    private int seats; // מספר מקומות ישיבה באוטובוס
}
