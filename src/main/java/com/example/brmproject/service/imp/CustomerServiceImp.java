package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.CustomerDTO;
import com.example.brmproject.domain.entities.CustomerEntity;
import com.example.brmproject.exception.ResourceNotFoundException;
import com.example.brmproject.repositories.CustomerEntityRepository;
import com.example.brmproject.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImp implements CustomerService {

    private ModelMapper modelMapper;
    //DI here ...add inteface
    private CustomerEntityRepository customerRepository;
    @Autowired
    public CustomerServiceImp(ModelMapper modelMapper, CustomerEntityRepository customerRepository) {
        this.modelMapper = modelMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDto) {
        CustomerEntity customer= customerRepository.save(mapToEntity(customerDto));
        CustomerEntity newCustomer= customerRepository.findById(customer.getId()).orElseThrow(()->new ResourceNotFoundException("Customer","Id",String.valueOf(customer.getId())));

       return mapToDTO(newCustomer);


    }

    @Override
    public List<CustomerDTO> findAll() {

        List<CustomerDTO>list=customerRepository.findAll().stream().map(customers->mapToDTO(customers)).collect(Collectors.toList());

        return list;
    }

    @Override
    public CustomerDTO updateDebit(Integer customerId ,Double newDebit) {
        CustomerEntity customer=customerRepository.findById(customerId).orElseThrow(()->new ResourceNotFoundException("customer","id", String.valueOf(customerId)));
        if(customer!=null)
        {
            customer.setDebit(newDebit);
            customerRepository.save(customer);
        }

        return mapToDTO(customer);
    }

    @Override
    public CustomerDTO findOne(Integer customerId) {
         CustomerEntity customer =   customerRepository.findById(customerId).orElseThrow(()->new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        return mapToDTO(customer);
    }


    //map to dto
    public CustomerDTO mapToDTO(CustomerEntity customer) {
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;

    }

    public CustomerEntity mapToEntity(CustomerDTO customerDTO) {
        CustomerEntity customer = modelMapper.map(customerDTO, CustomerEntity.class);
        return customer;

    }

}
