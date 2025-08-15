package com.schoolapp.student_management;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    @Query("SELECT t FROM Tracking t WHERE " +
           "t.studentClass = :cls AND t.stream = :str AND " +
           "t.monDate BETWEEN :from AND :to")
    List<Tracking> findByClassStreamAndDateRange(
        @Param("cls") String cls,
        @Param("str") String str,
        @Param("from") LocalDate from,
        @Param("to") LocalDate to
    );

    // ✅ NEW: find by student + class + stream + monDate
    Optional<Tracking> findByStudentIdAndStudentClassAndStreamAndMonDate(
        String studentId,
        String studentClass,
        String stream,
        LocalDate monDate
    );

    // ✅ NEW: find records where any weekday date matches the selected date
    @Query("SELECT t FROM Tracking t WHERE " +
           "t.monDate = :date OR t.tueDate = :date OR t.wedDate = :date OR " +
           "t.thursDate = :date OR t.friDate = :date OR t.satDate = :date")
    List<Tracking> findByAnyMatchingDate(@Param("date") LocalDate date);

    @Query("SELECT t FROM Tracking t WHERE " +
       ":date IN (t.monDate, t.tueDate, t.wedDate, t.thursDate, t.friDate, t.satDate)")
List<Tracking> findByAnyDate(@Param("date") LocalDate date);

}
