package com.example.travle.services;

import java.util.*;
import com.example.travle.models.*;
import com.example.travle.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

@Service
public class PublicTransportService {

    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private LineStationRepository lineStationRepository;
    @Autowired
    private StationRepository stationRepository;

    // --- א. פונקציות מנהל ---

    // 1. הוספת קו חדש עם כל התחנות שלו
    public String addNewLineWithStations(String lineNum, String src, String dest, String stationsStr) {
        Line line = new Line();
        line.setNumber(lineNum);
        line.setSource(src);
        line.setDestination(dest);
        lineRepository.save(line);

        String[] names = stationsStr.split(",");
        for (int i = 0; i < names.length; i++) {
            saveStationToLine(line, names[i].trim(), i + 1);
        }
        return "קו " + lineNum + " נוצר בהצלחה עם המסלול המלא.";
    }

    // 2. עדכון מסלול לפי מספר קו (משפיע על כל הנסיעות של הקו)
    public String updateLinePath(String lineNum, String stationName, int order, boolean isAdd) {
        Line line = lineRepository.findAll().stream()
                .filter(l -> l.getNumber().equals(lineNum)).findFirst()
                .orElseThrow(() -> new RuntimeException("קו לא נמצא"));

        if (isAdd) {
            // הזזת תחנות קיימות קדימה
            lineStationRepository.findAll().stream()
                    .filter(ls -> ls.getLine().getId().equals(line.getId()) && ls.getStationOrder() >= order)
                    .forEach(ls -> {
                        ls.setStationOrder(ls.getStationOrder() + 1);
                        lineStationRepository.save(ls);
                    });
            saveStationToLine(line, stationName, order);
            return "תחנה נוספה למסלול של קו " + lineNum;
        } else {
            // הסרה וצמצום רווחים
            lineStationRepository.findAll().stream()
                    .filter(ls -> ls.getLine().getId().equals(line.getId())
                            && ls.getStation().getName().trim().equalsIgnoreCase(stationName.trim()))
                    .findFirst().ifPresent(ls -> {
                        int removedOrder = ls.getStationOrder();
                        lineStationRepository.delete(ls);
                        lineStationRepository.findAll().stream()
                                .filter(lss -> lss.getLine().getId().equals(line.getId())
                                        && lss.getStationOrder() > removedOrder)
                                .forEach(lss -> {
                                    lss.setStationOrder(lss.getStationOrder() - 1);
                                    lineStationRepository.save(lss);
                                });
                    });
            return "תחנה הוסרה ממסלול קו " + lineNum;
        }
    }

    // פונקציה עבור דרישות ב.2 ו-ב.3
    public List<String> getLineStationsNames(String lineNum) {
        return lineStationRepository.findAll().stream()
                .filter(ls -> ls.getLine().getNumber().equals(lineNum))
                .sorted(Comparator.comparingInt(LineStation::getStationOrder))
                .map(ls -> ls.getStationOrder() + ". " + ls.getStation().getName())
                .toList();
    }

    // 3. הוספת נסיעה - המסלול נלקח אוטומטית מהקו
    public String addTravelToLine(String lineNum, String timeStr) {
        Line line = lineRepository.findAll().stream()
                .filter(l -> l.getNumber().equals(lineNum)).findFirst().orElse(null);
        if (line == null)
            return "שגיאה: קו " + lineNum + " לא קיים. צור אותו קודם.";

        Travel travel = new Travel();
        travel.setLine(line);
        travel.setDepartureTime(LocalTime.parse(timeStr));
        travelRepository.save(travel);
        return "נסיעה חדשה לקו " + lineNum + " נוספה לשעה " + timeStr;
    }

    // --- ב. פונקציות נוסע (קל קו) ---

    // 1. זמני הגעה לפי שם תחנה ומספר קו
    public String getArrivalTimes(String lineNum, String stationName) {
        // 1. שליפת כל הנסיעות של הקו הספציפי
        List<Travel> travels = travelRepository.findByLineNumber(lineNum);

        if (travels.isEmpty()) {
            return "לא נמצאו נסיעות מתוכננות לקו " + lineNum;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<h3>לוח זמני הגעה לקו ").append(lineNum).append(" בתחנת ").append(stationName).append(":</h3>");
        sb.append("<ul>");

        boolean foundAtLeastOne = false;

        for (Travel t : travels) {
            // 2. חיפוש המיקום של התחנה במסלול של הקו הזה
            // משתמשים ב-equalsIgnoreCase כדי למנוע בעיות של רווחים או אותיות
            Optional<LineStation> lsOpt = lineStationRepository.findAll().stream()
                    .filter(ls -> ls.getLine().getId().equals(t.getLine().getId()) &&
                            ls.getStation().getName().trim().equalsIgnoreCase(stationName.trim()))
                    .findFirst();

            if (lsOpt.isPresent()) {
                foundAtLeastOne = true;
                int order = lsOpt.get().getStationOrder();

                // חישוב זמן הגעה מוערך: זמן יציאה + (מיקום התחנה * 2 דקות)
                // אם תחנה ראשונה (order=1) -> מגיע בזמן היציאה
                LocalTime arrivalTime = t.getDepartureTime().plusMinutes((order - 1) * 2);

                sb.append("<li>יצא מהמסוף ב-<b>").append(t.getDepartureTime())
                        .append("</b> | מגיע לתחנה ב: <b>").append(arrivalTime).append("</b></li>");
            }
        }

        sb.append("</ul>");

        if (!foundAtLeastOne) {
            return "התחנה '" + stationName + "' לא נמצאה במסלול של קו " + lineNum
                    + ". <br>בדקי את רשימת התחנות של הקו.";
        }

        return sb.toString();
    }

    // --- ב.4: נסיעות בשעה הקרובה מתחנה מסוימת ---
    public String getUpcomingTravels(String stationName, String timeStr) {
        LocalTime searchTime = LocalTime.parse(timeStr);
        LocalTime limitTime = searchTime.plusHours(1);

        StringBuilder sb = new StringBuilder();
        sb.append("<h3>אוטובוסים שיגיעו לתחנת ").append(stationName).append(" בין ").append(searchTime).append(" ל-")
                .append(limitTime).append(":</h3>");
        sb.append("<ul>");

        boolean foundAtLeastOne = false;

        // 1. עוברים על כל הנסיעות הקיימות במערכת (מכל הקווים)
        List<Travel> allTravels = travelRepository.findAll();

        for (Travel t : allTravels) {
            // 2. בודקים האם התחנה הזו קיימת במסלול של הקו הספציפי הזה
            Optional<LineStation> lsOpt = lineStationRepository.findAll().stream()
                    .filter(ls -> ls.getLine().getId().equals(t.getLine().getId()) &&
                            ls.getStation().getName().trim().equalsIgnoreCase(stationName.trim()))
                    .findFirst();

            if (lsOpt.isPresent()) {
                // 3. חישוב זמן ההגעה המדויק לתחנה הזו (זמן יציאה + 2 דקות לכל תחנה)
                int order = lsOpt.get().getStationOrder();
                LocalTime arrivalAtStation = t.getDepartureTime().plusMinutes((order - 1) * 2);

                // 4. בודקים האם זמן ההגעה נופל בטווח של השעה הקרובה מהזמן שהוזן
                if ((arrivalAtStation.isAfter(searchTime) || arrivalAtStation.equals(searchTime))
                        && arrivalAtStation.isBefore(limitTime)) {

                    foundAtLeastOne = true;
                    sb.append("<li>🚌 <b>קו ").append(t.getLine().getNumber()).append("</b> | ")
                            .append("מגיע ב-<b>").append(arrivalAtStation).append("</b> ")
                            .append("(מוצא: ").append(t.getLine().getSource()).append(")</li>");
                }
            }
        }

        sb.append("</ul>");

        if (!foundAtLeastOne) {
            return "לא נמצאו נסיעות שיגיעו לתחנת '" + stationName + "' בשעה הקרובה (בין " + searchTime + " ל-"
                    + limitTime + ").";
        }

        return sb.toString();
    }

    // 5. נסיעה אחרונה ביום
    // 5. נסיעה אחרונה ביום - מתוקן
    public String getLastTravel(String lineNum) {
        // שליפת כל הנסיעות של הקו
        List<Travel> travels = travelRepository.findByLineNumber(lineNum);

        if (travels == null || travels.isEmpty()) {
            return "אין נסיעות רשומות לקו " + lineNum;
        }

        // מציאת הנסיעה עם זמן היציאה המקסימלי (הכי מאוחר)
        Travel lastTravel = travels.stream()
                .max(Comparator.comparing(t -> t.getDepartureTime()))
                .get();

        return "הנסיעה האחרונה להיום עבור קו " + lineNum + " יוצאת ב: <b>" + lastTravel.getDepartureTime() + "</b>";
    }

    // פונקציית עזר לשמירת תחנה
    private void saveStationToLine(Line line, String name, int order) {
        Station s = new Station();
        s.setName(name);
        stationRepository.save(s);
        LineStation ls = new LineStation();
        ls.setLine(line);
        ls.setStation(s);
        ls.setStationOrder(order);
        lineStationRepository.save(ls);
    }
}