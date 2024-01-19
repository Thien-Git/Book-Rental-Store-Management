package com.example.brmproject.domain.dto;

import com.example.brmproject.domain.entities.CustomerEntity;
import com.example.brmproject.domain.entities.OrderDetailEntity;
import com.example.brmproject.domain.entities.StaffEntity;
import com.example.brmproject.ultilities.SD.OrderStatus;
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
public class OrdersDTO {
    private int id;

    private String rentDate;

    private String bookingDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private String returnDate;

    private Integer rentDayAmount;

    private Double totalAmount;

    private Integer employeeId;

    private Integer customerId;

    private Collection<OrderDetailDTO> orderDetailsById;

    private StaffDTO staffByEmployeeId;

    private CustomerDTO customerByCustomerId;

    public void addOrderDetailDTO(OrderDetailDTO orderDetail) {
        orderDetailsById.add(orderDetail);
        orderDetail.setOrdersByOrderId(this);
    }

    public void removeOrderDetail(OrderDetailDTO orderDetail) {
        orderDetailsById.remove(orderDetail);
        orderDetail.setOrdersByOrderId(null);
    }
}
