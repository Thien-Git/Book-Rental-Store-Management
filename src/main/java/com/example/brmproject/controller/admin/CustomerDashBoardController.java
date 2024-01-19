package com.example.brmproject.controller.admin;

import com.example.brmproject.domain.dto.CustomerDTO;
import com.example.brmproject.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CustomerDashBoardController {


    CustomerService customerService;
    @Autowired
    public CustomerDashBoardController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/staff/customers/getAll")
    public String allCustomer(Model model, @RequestHeader HttpHeaders headers) {
        List<CustomerDTO> allCustomer = customerService.findAll();
        // In ra các header
        model.addAttribute("allCustomer", allCustomer);
        return "adminTemplate/customerDashboard/CustomerDashBoard";
    }

    @GetMapping("/staff/customer/add-debit/{customerId}")
    public String addDebit(Model model, @PathVariable Integer customerId)
    {
        CustomerDTO cusDTO= customerService.findOne(customerId);
        model.addAttribute("customer",cusDTO);
        return "adminTemplate/customerDashboard/customer-detail";
    }


    @PostMapping("/staff/customer/add-debit")
    public String addDebit(Model model, @ModelAttribute("customer") @Valid CustomerDTO cusDTO, BindingResult bindingResult)
    {
        try{
        if(bindingResult.hasErrors())
        {
            model.addAttribute("customer",cusDTO);

            return "adminTemplate/customerDashboard/customer-detail";
        }
        Double newDebit=cusDTO.getDebit()+cusDTO.getAddDebit();
        CustomerDTO newCustomer=customerService.updateDebit(cusDTO.getId(),newDebit);
        if(newCustomer!=null){
        model.addAttribute("alertMessage", "Congratulation!Add debit success!");
        model.addAttribute("customer",newCustomer);
        }
        else
        {
            model.addAttribute("alertError", "Oops!Something wrong!");

        }
            return "adminTemplate/customerDashboard/customer-detail";
        }catch (Exception e)
        {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }


}
