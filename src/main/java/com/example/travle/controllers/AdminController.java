package com.example.travle.controllers;

import com.example.travle.services.PublicTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private PublicTransportService transportService;

    @GetMapping("/add-line") // א.1
    public String addLine(@RequestParam String line, @RequestParam String src, @RequestParam String dest,
            @RequestParam String stations) {
        return transportService.addNewLineWithStations(line, src, dest, stations);
    }

    @GetMapping("/update-path") // א.2
    public String updatePath(@RequestParam String line, @RequestParam String station, @RequestParam int order,
            @RequestParam boolean add) {
        return transportService.updateLinePath(line, station, order, add);
    }

    @GetMapping("/add-travel") // א.3
    public String addTravel(@RequestParam String line, @RequestParam String time) {
        return transportService.addTravelToLine(line, time);
    }
}