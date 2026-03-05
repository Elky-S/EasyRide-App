package com.example.travle.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * מודל המייצג נהג במערכת התחבורה.
 * כולל פרטי התקשרות ומדד איכות (דירוג).
 */
@Entity
@Table(name = "drivers")
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // קוד נהג ייחודי (Primary Key)

    private String name; // שם מלא של הנהג
    private String phone; // מספר טלפון ליצירת קשר
    private double rating; // דירוג הנהג (מדד שביעות רצון)
}