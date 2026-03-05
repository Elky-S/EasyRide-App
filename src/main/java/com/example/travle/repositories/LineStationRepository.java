package com.example.travle.repositories;

import com.example.travle.models.LineStation;
import com.example.travle.models.LineStationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ממשק לניהול סדר התחנות במסלולי הקווים.
 * משתמש במפתח מורכב (LineStationId).
 */
@Repository
public interface LineStationRepository extends JpaRepository<LineStation, LineStationId> {
}