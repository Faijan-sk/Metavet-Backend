package com.example.demo.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.DayOfWeek;

@Repository
public interface DoctorDaysRepo extends JpaRepository<DoctorDays, Long> {
    
    @Query("SELECT d FROM DoctorDays d WHERE d.doctor = :doctor")
    List<DoctorDays> findByDoctorId(@Param("doctor") DoctorsEntity doctor);

    @Query("SELECT d FROM DoctorDays d WHERE d.doctor = :doctor")
    List<DoctorDays> findByDoctor(DoctorsEntity doctor);

    List<DoctorDays> findByDoctor_DoctorId(Long doctorId);

    void deleteByDoctor_DoctorId(Long doctorId);

    List<DoctorDays> findByDayOfWeek(DayOfWeek day);

    @Query("SELECT DISTINCT d.doctor FROM DoctorDays d WHERE d.dayOfWeek = :day")
    List<DoctorsEntity> findDoctorsByDay(@Param("day") DayOfWeek day);
}