package com.example.travle.repositories;

import com.example.travle.models.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ממשק לניהול פעולות מול טבלת האוטובוסים.
 */
@Repository
public interface BusRepository extends JpaRepository<Bus, Long> {
}