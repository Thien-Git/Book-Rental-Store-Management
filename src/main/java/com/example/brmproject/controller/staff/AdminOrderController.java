package com.example.brmproject.controller.staff;

import com.example.brmproject.domain.dto.OrdersDTO;
import com.example.brmproject.service.BookDetailService;
import com.example.brmproject.service.BookService;
import com.example.brmproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.example.brmproject.ultilities.StaticFunction.setStatus;
import static com.example.brmproject.ultilities.StaticFunction.showAlert;

@Controller
@SessionAttributes("session")
@RequestMapping("staff/orders")
public class AdminOrderController {
    private OrderService service;
    private BookDetailService bdService;
    private BookService bookService;
    @Autowired
    public AdminOrderController(OrderService service, BookDetailService bdService, BookService bookService) {
        this.service = service;
        this.bdService = bdService;
        this.bookService = bookService;
    }


    @GetMapping("/rent-confirm-order/{orderId}")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String rentOrder(Model model, @PathVariable Integer orderId, RedirectAttributes redirectAttributes)
    {
        try{
            service.rentOrder(orderId);

            redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Confirm rend success!! ");

            return "redirect:/staff/orders/show-all";
        }catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }


    @GetMapping("/return-confirm-order/{orderId}")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String returnOrder(Model model, @PathVariable Integer orderId,RedirectAttributes redirectAttributes)
    {
         try{
                service.completedOrder(orderId);

                redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Confirm return success!!");
                 return "redirect:/staff/orders/show-all";
        }catch ( Exception e) {
                model.addAttribute("message",e.getMessage());
        return "/error";
        }
    }

    @GetMapping("/show-all")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String showAll(Model model, @ModelAttribute("alertMessage") String alertMessage, @ModelAttribute("alertError") String alertError)
    {
        try {
            setStatus(model);
            showAlert(model,alertMessage,alertError);

            List<OrdersDTO> list=service.findAll();
            model.addAttribute("list",list);
            return "adminTemplate/order/order-show";
        }catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }
    @GetMapping("/show-by-status/{status}")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String showByStatus(Model model,@PathVariable String status,@ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError)
    {
        try {
            setStatus(model);
            showAlert(model,alertMessage,alertError);
            List<OrdersDTO> list=service.findByStatus(status);
            model.addAttribute("list",list);
            return "adminTemplate/order/order-show";

        }catch ( Exception e) {
            model.addAttribute("message",e.getMessage());
            return "/error";
        }
    }
    @GetMapping("/order-detail/{orderId}")
    @PreAuthorize("hasAuthority('STAFF') or hasAuthority('ADMIN')")
    public String orderDetail(Model model,@PathVariable Integer orderId,@ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError)
    {

        try{
            setStatus(model);
            showAlert(model,alertMessage,alertError);
            OrdersDTO order= service.findById(orderId);
            model.addAttribute("order",order);

            return "adminTemplate/order/order-detail";
            }catch (Exception e) {
                 model.addAttribute("message",e.getMessage());
                 return "/error";
             }
    }
}
