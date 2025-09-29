package com.liquidator.app.search.service;

import com.liquidator.app.search.entity.Doctor;
import com.liquidator.app.search.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> searchDoctorsBySpeciality(String query) {
        return doctorRepository.searchBySpeciality(query);
    }
}

