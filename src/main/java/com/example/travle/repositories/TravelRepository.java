package com.example.travle.repositories;

import com.example.travle.models.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// @Repository
// public interface TravelRepository extends JpaRepository<Travel, Long> {
//     // פונקציה שמוצאת את כל הנסיעות המשויכות למספר קו מסוים (למשל "422")
//     List<Travel> findByLineNumber(String lineNumber);
// }
@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    @Query("SELECT t FROM Travel t WHERE t.line.number = :lineNumber")
    List<Travel> findByLineNumber(@Param("lineNumber") String lineNumber);
}