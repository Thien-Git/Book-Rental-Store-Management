package com.example.brmproject.domain.dto;

import com.example.brmproject.domain.entities.OrdersEntity;
import com.example.brmproject.domain.entities.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StaffDTO {
     private int id;
     private String name;
     private String employeeCode;
     private String email;
     private Integer userId;
//     private Collection<OrdersDTO> ordersById;
//     private UserDTO userByUserId;
}
