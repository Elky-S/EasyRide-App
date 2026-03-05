package com.example.travle.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * מודל המייצג תחנת אוטובוס פיזית.
 */
@Entity
@Table(name = "stations")
@Data
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // קוד תחנה ייחודי

    private String name; // שם התחנה (למשל "מרכזית ירושלים")
}