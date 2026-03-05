package com.example.travle.controllers;

import com.example.travle.services.PublicTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/passenger")
public class PassengerController {

    @Autowired
    private PublicTransportService transportService;

    // 1. זמני הגעה לפי שם תחנה ומספר קו
    @GetMapping("/arrival")
    public String getArrival(@RequestParam String line, @RequestParam String station) {
        return transportService.getArrivalTimes(line, station);
    }

    // 2+3. קבלת כל שמות התחנות במסלול (שמיעת תחנות / מיקומים)
    @GetMapping("/stations")
    public List<String> getStations(@RequestParam String line) {
        // ודאי שב-Service הפונקציה נקראת בדיוק ככה: getLineStationsNames
        return transportService.getLineStationsNames(line);
    }

    // 4. נסיעות בשעה הקרובה
    @GetMapping("/upcoming")
    public String getUpcoming(@RequestParam String station, @RequestParam String time) {
        return transportService.getUpcomingTravels(station, time);
    }

    // 5. נסיעה אחרונה ביום
    @GetMapping("/last")
    public String getLast(@RequestParam String line) {
        return transportService.getLastTravel(line);
    }
}