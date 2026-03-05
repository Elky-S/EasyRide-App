package com.example.travle.repositories;

import com.example.travle.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ממשק לניהול פעולות מול טבלת הנהגים.
 * מאפשר שמירה, עדכון ושליפה של נהגים ממאגר הנתונים.
 */
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
}