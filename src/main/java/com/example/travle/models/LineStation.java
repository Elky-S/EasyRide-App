package com.example.travle.models;

import jakarta.persistence.*;
import lombok.Data;

/**
 * טבלת עזר לניהול הקשר בין תחנות לקווים (רבים לרבים).
 * קובעת את סדר התחנות בכל קו.
 */
@Entity
@Table(name = "line_stations")
@IdClass(LineStationId.class) // מחברת את המפתח המורכב שיצרנו בשלב 1
@Data
public class LineStation {

    @Id
    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line; // קוד הקו (חלק מהמפתח)

    @Id
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station; // קוד התחנה (חלק מהמפתח)

    @Column(name = "station_order")
    private int stationOrder; // מספר התחנה במסלול (למשל: תחנה מספר 1, 2...)
}