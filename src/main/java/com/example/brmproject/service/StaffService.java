package com.example.brmproject.service;

import java.util.List;

import com.example.brmproject.domain.dto.StaffDTO;

public interface StaffService {
    StaffDTO createStaff(StaffDTO staff);
    List<StaffDTO> findAll();

    
} 