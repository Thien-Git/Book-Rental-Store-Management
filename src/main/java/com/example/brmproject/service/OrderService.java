package com.example.brmproject.service;

import com.example.brmproject.domain.dto.OrdersDTO;

import java.util.List;

public interface OrderService {

    OrdersDTO createOrder(List<Integer> bookIdList,OrdersDTO ordersDTO);
    List<OrdersDTO> findAll();
    List<OrdersDTO> findByStatus(String status);

    OrdersDTO updateOrder(OrdersDTO ordersDTO);
    OrdersDTO findById(Integer id);
    List<OrdersDTO> getAllOrdersOfUser(int userId);


    void rentOrder(Integer orderId);
    OrdersDTO completedOrder(Integer orderId);






}
