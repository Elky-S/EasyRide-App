package com.example.travle.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalTime;

/**
 * מודל המייצג נסיעה ספציפית בלוח הזמנים.
 * מחבר בין אוטובוס, נהג וקו לזמן יציאה מסוים.
 */
@Entity
@Table(name = "travels")
@Data
public class Travel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // קוד נסיעה ייחודי

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Bus bus; // האוטובוס המוקצה לנסיעה זו (Foreign Key)

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver; // הנהג המבצע את הנסיעה (Foreign Key)

    @ManyToOne
    @JoinColumn(name = "line_id")
    private Line line; // הקו אליו שייכת הנסיעה (Foreign Key)

    private LocalTime departureTime; // שעת יציאת האוטובוס מהתחנה הראשונה
}