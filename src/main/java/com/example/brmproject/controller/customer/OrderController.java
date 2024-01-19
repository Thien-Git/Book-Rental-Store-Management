package com.example.brmproject.controller.customer;


import com.example.brmproject.controller.auth.AuthenticationHelper;
import com.example.brmproject.domain.dto.*;
import com.example.brmproject.service.*;
import com.example.brmproject.ultilities.StaticFunction;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
@SessionAttributes("session")
@RequestMapping("customers")
public class OrderController {

    private OrderService service;
    private OrderDetailService odService;
    private BookDetailService bdService;
    private BookService bookService;
    private AuthenticationHelper authenticationHelper;
    private CustomerService customerService;

    @Autowired
    public OrderController(OrderService service,
                           BookDetailService bdService,
                           OrderDetailService odService,
                           BookService bookService,
                           AuthenticationHelper authenticationHelper,
                            CustomerService customerService
    ) {
        this.service = service;
        this.odService = odService;
        this.bdService = bdService;
        this.bookService = bookService;
        this.authenticationHelper = authenticationHelper;
        this.customerService=customerService;
    }

    @ModelAttribute("session")
    public MySession addSessionToModel() {
        return new MySession(new ArrayList<>());
    }

    @GetMapping("/addItem/{bookId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String addItemToOrder(@ModelAttribute("session") MySession session, Model model, @PathVariable String bookId, RedirectAttributes redirectAttributes) {
        //check dublicate
        BookDTO bookDTO = bdService.countAvailable(Integer.parseInt(bookId));

        if (session.getBookIdList().contains(Integer.parseInt(bookId))) {
            redirectAttributes.addAttribute("alertError", "This book was added to your list!");
        }
        //check max orderdetail
        else if (session.getBookIdList().size() > 5) {
            redirectAttributes.addAttribute("alertError", "Sorry! Maximum number of books to rend is 5!");
        }
        //check available
        else if (bookDTO.getAvailableBook() <= 0) {
            redirectAttributes.addAttribute("alertError", "Sorry! This book is not available now!");
        } else {
            session.getBookIdList().add(Integer.parseInt(bookId));
            redirectAttributes.addAttribute("alertMessage", "Add book to cart success!!");

        }

        //check available
        return "redirect:/customers/books";
    }
    //add in detail


    @GetMapping("/addItemInDetail/{bookId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String addItemToOrderInDetail(@ModelAttribute("session") MySession session, Model model, @PathVariable String bookId, RedirectAttributes redirectAttributes) {
        //check dublicate
        BookDTO bookDTO = bdService.countAvailable(Integer.parseInt(bookId));

        if (session.getBookIdList().contains(Integer.parseInt(bookId))) {

            redirectAttributes.addAttribute("alertError", "Duplicate!This book has been added to your cart already !");
        }
        //check max orderdetail
        else if (session.getBookIdList().size() > 5) {
            redirectAttributes.addAttribute("alertError", "Sorry! Maximum number of books to rend is 5!");
        }
        //check available
        else if (bookDTO.getAvailableBook() <= 0) {
            redirectAttributes.addAttribute("alertError", "Sorry! This book is not available now!");
        } else {
            redirectAttributes.addAttribute("alertMessage", "Add book to cart success!");
            session.getBookIdList().add(Integer.parseInt(bookId));
        }
        //check available
        return "redirect:/customers/books/detail/{bookId}";
    }


    @GetMapping("/removeBook/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String removeBook(@ModelAttribute("session") MySession session, @PathVariable Integer id) {
        //find bd in session have bookid=id.

        session.getBookIdList().remove(Integer.valueOf(id));
        return "redirect:/customers/showCart";
    }

    @GetMapping("/removeAllBook")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String removeAllBook(@ModelAttribute("session") MySession session) {
        //change book detail status

        session.getBookIdList().clear();

        return "redirect:/customers/books";
    }


    @GetMapping("/showCart")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String showCart(@ModelAttribute("session") MySession session, Model model,@ModelAttribute("alertMessage") String alertMessage,
                           @ModelAttribute("alertError") String alertError) {

        StaticFunction.showAlert(model,alertMessage,alertError);
        List<Integer> bookIds = session.getBookIdList();
        List<BookDTO> list = bookService.getListBookByBookId(bookIds);
        model.addAttribute("books", list);
        model.addAttribute("orderForm", new OrderFormDTO());

        int userId = authenticationHelper.getUserIdFromAuthentication();
        double customerDebit = customerService.findOne(userId).getDebit();
        model.addAttribute("debit", customerDebit);
        return "customerTemplate/orders/cart";
    }

    @PostMapping("/createOrder")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public String createOrder(@ModelAttribute("orderForm") @Valid OrderFormDTO orderForm,BindingResult bindingResult,@ModelAttribute("session")MySession session , Model model, RedirectAttributes redirectAttributes)
    {
    try{

    if (bindingResult.hasErrors()) {

                List<Integer> bookIds = session.getBookIdList();
                List<BookDTO> list = bookService.getListBookByBookId(bookIds);
                model.addAttribute("books", list);
                model.addAttribute("orderForm", orderForm);
                return "customerTemplate/orders/cart";
            }
            if (session.getBookIdList().isEmpty()) {
                redirectAttributes.addFlashAttribute("alertError", "You have to choose book first!");
                return "redirect:/customers/books";
            }
            //check stock
            boolean flag = false;
            List<Integer> bookIdList=new ArrayList<>(session.getBookIdList());
            Iterator<Integer> iterator = bookIdList.iterator();
            while (iterator.hasNext())
            {
                Integer bookId= iterator.next();
                BookDTO availableBook = bdService.countAvailable(bookId);
                if (availableBook == null || availableBook.getAvailableBook() <= 0) {
                    session.getBookIdList().remove(Integer.valueOf(bookId));
                    flag = true;
                }
            }
            if (flag) {
                redirectAttributes.addFlashAttribute("alertError", "Some of your order book not available anymore!");
                return "redirect:/customers/showCart";
            }
//
            OrdersDTO myOrderDTO = new OrdersDTO();
            myOrderDTO.setRentDayAmount(orderForm.getRentDays());
            //gang cung test
            Integer customerId = authenticationHelper.getUserIdFromAuthentication();
            myOrderDTO.setCustomerId(customerId);

//null
            OrdersDTO dto = service.createOrder(session.getBookIdList(), myOrderDTO);
            //update bookdetail status.
            if (dto != null) {
                redirectAttributes.addFlashAttribute("alertMessage", "booking success!");

                session.getBookIdList().clear();
                return "redirect:/customers/books";
            }
            else{
                redirectAttributes.addFlashAttribute("alertError", "Not enough money in your debit. Must > $20 !");
                return "redirect:/customers/showCart";
            }


        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "/error";
        }

    }

    //staff


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/orders")
    public String orders(Model model) {
        int userId = authenticationHelper.getUserIdFromAuthentication();
        List<OrdersDTO> orderList = service.getAllOrdersOfUser(userId);
        model.addAttribute("orders", orderList);
        model.addAttribute("amount", orderList.size());

        return "customerTemplate/orders/orderList";
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @GetMapping("/orders/{orderId}")
    public String order(@PathVariable("orderId") int orderId, Model model) {
        int userId = authenticationHelper.getUserIdFromAuthentication();
        List<OrderDetailDTO> orderDetailList = odService.getOrdersDetail(orderId, userId);
        model.addAttribute("orderDetails", orderDetailList);

        int order = orderDetailList.get(0).getOrderId();
        model.addAttribute("orderId", order);

        return "customerTemplate/orders/orderDetail";
    }
}
