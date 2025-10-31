package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Service.DoctorDaysService;

@RestController
@RequestMapping("/api/doctor-days")
public class DoctorDaysController {

    @Autowired
    private DoctorDaysService doctorDaysService;

    // API 1: Create days for a doctor (already correct)
    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorDays>> addDoctorDays(
            @PathVariable long doctorId, 
            @RequestBody List<DayOfWeek> days) {
        List<DoctorDays> createdDays = doctorDaysService.createDaysForDoctor(doctorId, days);
        return ResponseEntity.ok(createdDays);
    }

    // API 2: Get available days for a specific doctor
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorDays>> getDoctorDays(@PathVariable long doctorId) {
        List<DoctorDays> days = doctorDaysService.getDoctorDaysFromDoctor(doctorId);
        return ResponseEntity.ok(days);
    }

    // API 3: Get all doctors available on a specific day
    @GetMapping("/day/{day}")
    public ResponseEntity<List<DoctorsEntity>> getDoctorsByDay(@PathVariable DayOfWeek day) {
        List<DoctorsEntity> doctors = doctorDaysService.getDoctorsByDay(day);
        return ResponseEntity.ok(doctors);
    }

    // API 4: Alternative - Get doctors by day using query parameter
    @GetMapping("/find-doctors")
    public ResponseEntity<List<DoctorsEntity>> findDoctorsByDay(@RequestParam DayOfWeek day) {
        List<DoctorsEntity> doctors = doctorDaysService.getDoctorsByDay(day);
        return ResponseEntity.ok(doctors);
    }
}