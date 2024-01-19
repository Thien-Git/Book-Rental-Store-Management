package com.example.brmproject.service;

import com.example.brmproject.domain.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    CustomerDTO createCustomer(CustomerDTO customer);
    List<CustomerDTO> findAll();

    CustomerDTO updateDebit(Integer customerId,Double newDebit);
    CustomerDTO findOne(Integer customerId);


}
