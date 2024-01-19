package com.example.brmproject.controller.staff;

import com.example.brmproject.domain.dto.OrderDetailDTO;
import com.example.brmproject.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("staff/order-detail")
public class AdminOrderDetailController {

    private OrderDetailService service;

    @Autowired
    public AdminOrderDetailController(OrderDetailService service) {
        this.service = service;
    }


    @GetMapping("/mark-lost/{orderDetailId}")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String lostOrderDetail(Model model, @PathVariable Integer orderDetailId)
    {
        OrderDetailDTO orderDetail=service.markAsLost(orderDetailId);
        Integer myOrderId=orderDetail.getOrderId();


        return "redirect:/staff/orders/order-detail/"+myOrderId;
    }
}
