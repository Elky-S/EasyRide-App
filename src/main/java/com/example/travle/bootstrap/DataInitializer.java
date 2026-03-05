package com.example.travle.bootstrap;

import com.example.travle.models.*;
import com.example.travle.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

/**
 * מחלקה האחראית על אתחול הנתונים במערכת עם עליית השרת.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final DriverRepository driverRepo;
    private final BusRepository busRepo;
    private final LineRepository lineRepo;
    private final StationRepository stationRepo;
    private final LineStationRepository lsRepo;
    private final TravelRepository travelRepo;
    private final MockDataCreator dataCreator;

    public DataInitializer(DriverRepository driverRepo, BusRepository busRepo, LineRepository lineRepo,
            StationRepository stationRepo, LineStationRepository lsRepo,
            TravelRepository travelRepo, MockDataCreator dataCreator) {
        this.driverRepo = driverRepo;
        this.busRepo = busRepo;
        this.lineRepo = lineRepo;
        this.stationRepo = stationRepo;
        this.lsRepo = lsRepo;
        this.travelRepo = travelRepo;
        this.dataCreator = dataCreator;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> מתחיל אתחול נתונים מסודר...");

        // 1. יצירת תשתיות (נהג ואוטובוס) [cite: 4, 5]
        Driver adminDriver = dataCreator.createDefaultDriver(driverRepo);
        Bus mainBus = dataCreator.createDefaultBus(busRepo);

        // 2. הקמת קו 422 עם מסלול תחנות מלא [cite: 8, 12]
        Line line422 = dataCreator.createLine(lineRepo, "422", "ירושלים", "בני ברק");
        dataCreator.setupFullRoute(line422, stationRepo, lsRepo);

        // יצירת נסיעה לקו 422 [cite: 3, 15]
        dataCreator.createTravel(travelRepo, line422, adminDriver, mainBus, LocalTime.of(12, 0));

        // 3. הקמת קו 402 ובדיקת נסיעה אחרונה [cite: 26]
        Line line402 = dataCreator.createLine(lineRepo, "402", "ירושלים", "בני ברק");
        dataCreator.createTravel(travelRepo, line402, adminDriver, mainBus, LocalTime.of(22, 40));
        dataCreator.createTravel(travelRepo, line402, adminDriver, mainBus, LocalTime.of(22, 40));

        System.out.println(">>> המערכת מוכנה! (קווים: 422, 402)");
    }
}
