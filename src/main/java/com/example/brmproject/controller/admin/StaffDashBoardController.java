package com.example.brmproject.controller.admin;

import com.example.brmproject.domain.dto.SignupRequestDTO;
import com.example.brmproject.domain.dto.StaffDTO;
import com.example.brmproject.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Controller
public class StaffDashBoardController {

    StaffService staffService;

    @Autowired
    public StaffDashBoardController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/staff/employees/getAll")
    public String allStaff(Model model, @RequestHeader HttpHeaders headers) {
        List<StaffDTO> allStaff = staffService.findAll();
        // In ra các header
        model.addAttribute("allStaff", allStaff);
        model.addAttribute("messeage", "Xin chào");

        return "adminTemplate/staffDashboard/StaffDashboard";
    }

    @GetMapping("/staff/employees/create")
    public String createStaff(Model model){
        SignupRequestDTO signupRequestStaff = new SignupRequestDTO();
        model.addAttribute("signupRequestStaff", signupRequestStaff);
        return "adminTemplate/staffDashboard/CreateStaffDashboard";
    }

}
