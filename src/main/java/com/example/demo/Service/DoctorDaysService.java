package com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.DoctorDays;
import com.example.demo.Entities.DoctorsEntity;
import com.example.demo.Enum.DayOfWeek;
import com.example.demo.Repository.DoctorDaysRepo;
import com.example.demo.Repository.DoctorRepo;

@Service
public class DoctorDaysService {
	
	 @Autowired
	    private DoctorDaysRepo doctorDaysRepository;
		 
	    @Autowired
	    private DoctorRepo doctorRepository;
		    
	    // Method to create multiple days for a doctor
	    public List<DoctorDays> createDaysForDoctor(long doctorId, List<DayOfWeek> days) {
	        DoctorsEntity doctor = doctorRepository.findById(doctorId)
	                .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + doctorId));
			 
	        List<DoctorDays> existing = doctorDaysRepository.findByDoctor_DoctorId(doctorId);
	        List<DoctorDays> createdDays = new ArrayList<>();
	        
	        for (DayOfWeek day : days) {
	            boolean exists = existing.stream().anyMatch(r -> r.getDayOfWeek() == day);
	            if (exists) {
	                throw new RuntimeException("Day already assigned to doctor: " + day);
	            }
	            DoctorDays doctorDay = new DoctorDays(day, doctor);
	            createdDays.add(doctorDaysRepository.save(doctorDay));
	        }
	        
	        return createdDays;
	    }
	
	    
	    public List<DoctorDays> getDoctorDaysFromDoctor(long doctorId){
	    	return doctorDaysRepository.findByDoctor_DoctorId(doctorId);
	    	
	    }
	   
	    
	    public List<DoctorsEntity> getDoctorsByDay(DayOfWeek day) {
	        return doctorDaysRepository.findDoctorsByDay(day);
	    }
	   
	
	
}
