package com.example.travle.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * מודל המייצג קו נסיעה (למשל קו 422).
 */
@Entity
@Table(name = "lines")
@Data
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // קוד קו במערכת (Primary Key)

    private String number; // מספר הקו הסידורי (למשל "10", "402")
    private String source; // עיר/תחנת מוצא
    private String destination; // עיר/תחנת יעד
}