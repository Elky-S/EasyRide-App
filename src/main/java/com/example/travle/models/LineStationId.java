package com.example.travle.models;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * מחלקה המייצגת את המפתח המורכב של תחנה במסלול.
 * מורכבת מקוד הקו וקוד התחנה יחד.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LineStationId implements Serializable {
    private Long line; // חלק ראשון מהמפתח - קוד הקו
    private Long station; // חלק שני מהמפתח - קוד התחנה
}
