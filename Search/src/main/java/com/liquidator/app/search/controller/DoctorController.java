package com.liquidator.app.search.controller;

import com.liquidator.app.search.entity.Doctor;
import com.liquidator.app.search.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/search")
    public List<Doctor> searchDoctors(@RequestParam("query") String query) {
        return doctorService.searchDoctorsBySpeciality(query);
    }
}

