package com.example.travle.repositories;

import com.example.travle.models.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * ממשק לניהול פעולות מול טבלת הקווים (למשל קו 422).
 */
@Repository
public interface LineRepository extends JpaRepository<Line, Long> {
}