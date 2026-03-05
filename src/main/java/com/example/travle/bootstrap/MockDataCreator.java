package com.example.travle.bootstrap;

import com.example.travle.models.*;
import com.example.travle.repositories.*;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

/**
 * מחלקת עזר ליצירת אובייקטים ונתוני בדיקה (Mock Data).
 */
@Service
public class MockDataCreator {

    public Driver createDefaultDriver(DriverRepository repo) {
        Driver d = new Driver();
        d.setName("אלקי מנהלת");
        d.setPhone("050-0000000");
        d.setRating(5.0);
        return repo.save(d);
    }

    public Bus createDefaultBus(BusRepository repo) {
        Bus b = new Bus();
        b.setLicensePlate("12-345-67");
        b.setSeats(50);
        return repo.save(b);
    }

    public Line createLine(LineRepository repo, String num, String src, String dest) {
        Line l = new Line();
        l.setNumber(num);
        l.setSource(src);
        l.setDestination(dest);
        return repo.save(l);
    }

    /**
     * מקים מסלול תחנות עבור קו (הנחת דקה בין תחנה לתחנה)[cite: 12, 16].
     */
    public void setupFullRoute(Line line, StationRepository sRepo, LineStationRepository lsRepo) {
        String[] stations = { "תחנה מרכזית", "גבעת שאול", "מחלף חמד", "צומת שילת", "עזרא/נחמיה" };

        for (int i = 0; i < stations.length; i++) {
            Station s = new Station();
            s.setName(stations[i]);
            sRepo.save(s);

            LineStation ls = new LineStation();
            ls.setLine(line);
            ls.setStation(s);
            ls.setStationOrder(i + 1); // סדר התחנות במסלול [cite: 8]
            lsRepo.save(ls);
        }
    }

    public void createTravel(TravelRepository repo, Line l, Driver d, Bus b, LocalTime time) {
        Travel t = new Travel();
        t.setLine(l);
        t.setDriver(d);
        t.setBus(b);
        t.setDepartureTime(time);
        repo.save(t);
    }
}