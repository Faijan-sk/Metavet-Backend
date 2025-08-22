package com.example.demo.Controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.DoctorDtoForClient;
import com.example.demo.Service.DoctorService;

@RestController
@RequestMapping("/auth")
public class DoctorAuthController {
	
	@Autowired
	private DoctorService doctorService;
	
	
	 @GetMapping("/doctors/available")
	    public ResponseEntity<Map<String, Object>> getAvailableDoctors() {
	        Map<String, Object> response = new HashMap<>();
	        List<DoctorDtoForClient> doctors = doctorService.getAvailableAndActive();
	        response.put("success", true);
	        response.put("data", doctors);
	        response.put("count", doctors.size());
	        return ResponseEntity.ok(response);
	    }

	
	

}
