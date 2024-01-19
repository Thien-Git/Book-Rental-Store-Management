package com.example.brmproject.schedule;

import com.example.brmproject.domain.entities.BookDetailEntity;
import com.example.brmproject.domain.entities.CustomerEntity;
import com.example.brmproject.domain.entities.OrderDetailEntity;
import com.example.brmproject.domain.entities.OrdersEntity;
import com.example.brmproject.exception.ResourceNotFoundException;
import com.example.brmproject.repositories.*;
import com.example.brmproject.ultilities.SD.BookDetailStatus;
import com.example.brmproject.ultilities.SD.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class Schedule {

    private OrdersEntityRepository orderRepository;
    private CustomerEntityRepository customerRepostory;
    private OrderDetailEntityRepository ODRepository;
    private BookEntityRepository bookRepository;
    private BookDetailEntityRepository bookDetailRepository;
        @Autowired
    public Schedule(OrdersEntityRepository orderRepository, CustomerEntityRepository customerRepostory, OrderDetailEntityRepository ODRepository, BookEntityRepository bookRepository, BookDetailEntityRepository bookDetailRepository) {
        this.orderRepository = orderRepository;
        this.customerRepostory = customerRepostory;
        this.ODRepository = ODRepository;
        this.bookRepository = bookRepository;
        this.bookDetailRepository = bookDetailRepository;
    }

    @Scheduled(fixedRate = 86400000) // Thực hiện kiểm tra sau mỗi 24 h
    public void updateCancelOrder() {
        // Lấy danh sách các Order từ database
        String status= OrderStatus.BOOKING.toString();
        List<OrdersEntity> list=orderRepository.findByorderStatus(status).stream().collect(Collectors.toList());
        // Kiểm tra xem có Order nào gần tới timeHappen không

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (OrdersEntity order : list) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime timeHappen = LocalDateTime.parse(order.getBookingDate(),formatter);
            Duration duration = Duration.between(timeHappen,now);
            long seconds = duration.getSeconds();
            //neu6 HON 2 ngay thi2 cap65 nhat status booking->cancel
            if (seconds>2*24*60*60) {
                updateCancel(order);
            }

        }


    }

    //CHECK ORDER rent DAY +rentday amount :STATUS :RENT-> OVERDUE
    @Scheduled(fixedRate = 86400000) // Thực hiện kiểm tra sau mỗi 12 h
    public void updateOverdueOrder() {

        // Lấy danh sách các Order từ database
        String status=OrderStatus.RENT.toString();
        List<OrdersEntity> orders=orderRepository.findByorderStatus(status).stream().collect(Collectors.toList());
        // Kiểm tra xem có Order nào gần tới timeHappen không

        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (OrdersEntity order : orders) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime timeHappen = LocalDateTime.parse(order.getRentDate(),formatter);
            Duration duration = Duration.between(timeHappen,now);;
            long seconds = duration.getSeconds();
            long rentDayAmount=order.getRentDayAmount();
            //neu6 hon so  ngay muon thi2 cap65 nhat status booking->cancel
            if (seconds>24*60*60*rentDayAmount) {
                order.setOrderStatus(OrderStatus.OVERDUE.toString());
                orderRepository.save(order);
            }

        }
    }

    @Scheduled(fixedRate = 86400000) // Thực hiện kiểm tra sau mỗi 12 h
    public void updateLostOrder() {
        // Lấy danh sách các Order từ database
        String status=OrderStatus.OVERDUE.toString();
        List<OrdersEntity> orders=orderRepository.findByorderStatus(status).stream().collect(Collectors.toList());
        // Kiểm tra xem có Order loai overdue nào gần tới timeHappen không
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (OrdersEntity order : orders) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime timeHappen = LocalDateTime.parse(order.getRentDate(),formatter);
            Duration duration = Duration.between(timeHappen,now);
            long seconds = duration.getSeconds();
            //
            long overdueDayAmount=order.getRentDayAmount()+7;
            //neu con 7 ngay + so ngay muon thi cap nhat status booking->cancel
            if (seconds>24*60*60*overdueDayAmount) {
                updateLost(order);
            }

        }

    }


    public void updateCancel(OrdersEntity myOrder) {

        if (OrderStatus.BOOKING.toString().equals(myOrder.getOrderStatus())) {
            myOrder.setOrderStatus(OrderStatus.CANCEL.toString());
            List<OrderDetailEntity> myOrderDetails = myOrder.getOrderDetailsById().stream().toList();
            for (OrderDetailEntity od : myOrderDetails) {
                if (od.getBookDetailId() != null) {
                    BookDetailEntity bookDetail =  bookDetailRepository.findById(od.getBookDetailId()).orElseThrow(() -> new ResourceNotFoundException("book detail", "id", String.valueOf(od.getBookDetailId())));
                    bookDetail.setStatus(BookDetailStatus.AVAILABLE.toString());
                    bookDetailRepository.save(bookDetail);
                }
            }
            orderRepository.save(myOrder);
        }
    }
    public void updateLost(OrdersEntity myOrder)
    {
        if(myOrder.getOrderStatus().equals(OrderStatus.OVERDUE.toString()))
        {
            myOrder.setOrderStatus(OrderStatus.LOST.toString());
            List<OrderDetailEntity> myOrderDetails=myOrder.getOrderDetailsById().stream().toList();
            for (OrderDetailEntity od: myOrderDetails)
            {
                od.setLost(true);
                if(od.getBookDetailId()!=null)
                {
                    BookDetailEntity bookDetail=bookDetailRepository.findById(od.getBookDetailId()).orElseThrow(()->new ResourceNotFoundException("book detail","id",String.valueOf(od.getBookDetailId())));
                    bookDetail.setStatus(BookDetailStatus.LOST.toString());
                    bookDetailRepository.save(bookDetail);
                }
            }
            //   //subtract debit fine
            CustomerEntity customer= myOrder.getCustomerByCustomerId();
            Double fine=myOrder.getTotalAmount()/myOrder.getRentDayAmount()*20;
            Double debit=customer.getDebit();
            if(fine>=15)
            {
                fine=15.0;
            }
            debit-=fine;
            customer.setDebit(debit);
            customerRepostory.save(customer);
            orderRepository.save(myOrder);
        }
    }


}
