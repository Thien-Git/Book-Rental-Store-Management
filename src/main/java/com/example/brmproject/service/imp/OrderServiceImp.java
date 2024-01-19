package com.example.brmproject.service.imp;

import com.example.brmproject.controller.auth.AuthenticationHelper;
import com.example.brmproject.domain.dto.BookDetailDTO;
import com.example.brmproject.domain.dto.CustomerDTO;
import com.example.brmproject.domain.dto.OrderDetailDTO;
import com.example.brmproject.domain.dto.OrdersDTO;
import com.example.brmproject.domain.entities.*;
import com.example.brmproject.exception.ResourceNotFoundException;
import com.example.brmproject.repositories.*;
import com.example.brmproject.service.OrderService;
import com.example.brmproject.ultilities.SD.BookDetailStatus;
import com.example.brmproject.ultilities.SD.OrderStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {

   private OrdersEntityRepository orderRepository;
   private CustomerEntityRepository customerRepostory;
   private OrderDetailEntityRepository ODRepository;
   private BookEntityRepository bookRepository;
   private BookDetailEntityRepository bookDetailRepository;
    private AuthenticationHelper authenticationHelper;

    private ModelMapper modelMapper;
    @Autowired
    public OrderServiceImp(OrdersEntityRepository orderRepository, CustomerEntityRepository customerRepostory, OrderDetailEntityRepository ODRepository, BookEntityRepository bookRepository, BookDetailEntityRepository bookDetailRepository, AuthenticationHelper authenticationHelper, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.customerRepostory = customerRepostory;
        this.ODRepository = ODRepository;
        this.bookRepository = bookRepository;
        this.bookDetailRepository = bookDetailRepository;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
    }

    //create order with orderdetail, check debit....
    @Override
    public OrdersDTO createOrder( List<Integer> bookIdList,OrdersDTO ordersDTO) {

        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime bookingDateTime = LocalDateTime.now();
        String formattedNow = bookingDateTime.format(formatter);
        ordersDTO.setOrderStatus(OrderStatus.BOOKING);
        ordersDTO.setBookingDate(formattedNow);
        //check stock


        //total amount
        ordersDTO.setTotalAmount(countTotalAmount(bookIdList,ordersDTO.getRentDayAmount()));
        //get debit
        CustomerEntity customer=customerRepostory.findById(ordersDTO.getCustomerId()).get();
        double debit= customer.getDebit();
        //check debit
        if(debit-ordersDTO.getTotalAmount()>=20)
        {
            OrdersEntity order= orderRepository.save(mapToEntity(ordersDTO));
            OrdersEntity newOrder= orderRepository.findById(order.getId()).orElseThrow(()->new ResourceNotFoundException("Order","Id",String.valueOf(order.getId())));

          List<OrderDetailEntity> orderDetails=  createOrderDetail(bookIdList,newOrder.getId());
            changeBookDetailStatus(orderDetails);
            //add all list/
/// chuyen bd
            return mapToDTO(newOrder);
        }
        return null;
    }

    @Override
    public List<OrdersDTO> getAllOrdersOfUser(int userId) {
        List<OrdersEntity> ordersList = orderRepository.findAllByUserId(userId);

        return ordersList.stream().map(order -> mapToDTO(order)).collect(Collectors.toList());
    }
    @Override
    public List<OrdersDTO> findAll() {

        List<OrdersDTO>list=orderRepository.findAll().stream().map(orders->mapToDTO(orders)).collect(Collectors.toList());

        return list;

    }

    @Override
    public List<OrdersDTO> findByStatus(String status) {

        List<OrdersDTO>list=orderRepository.findByorderStatus(status).stream().map(orders->mapToDTO(orders)).collect(Collectors.toList());
        return list;
    }


    @Override
    public OrdersDTO updateOrder(OrdersDTO ordersDTO) {
        Integer orderId=ordersDTO.getId();
       OrdersEntity order= orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderId)));

       return mapToDTO(order);
    }

    @Override
    public OrdersDTO findById(Integer id) {
        if(id!=null){
            OrdersEntity order=orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order","Id",String.valueOf(id)));
                return mapToDTO(order);
        }
        return null;
    }


    @Override
    public void rentOrder(Integer orderId) {

        OrdersEntity order= orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderId)));
        OrdersDTO ordersDTO= mapToDTO(order);
             int userId = authenticationHelper.getUserIdFromAuthentication();
             order.setEmployeeId(userId);

            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime bookingDateTime = LocalDateTime.now();
            String formattedNow = bookingDateTime.format(formatter);
            Double debit=order.getCustomerByCustomerId().getDebit();
            //keep $20.
        if(order.getOrderStatus().equals(OrderStatus.BOOKING.toString()) && (debit-order.getTotalAmount()>=20))
        {
            updateBookDetailRent(mapToDTO(order));
            order.setOrderStatus(OrderStatus.RENT.toString());
            updateBookDetailRent(ordersDTO);
            order.setRentDate(formattedNow);
           CustomerEntity customer=  order.getCustomerByCustomerId();
           customer.setDebit(debit - order.getTotalAmount());
            customerRepostory.save(customer);
            orderRepository.save(order);
        }

    }

    @Override
    public OrdersDTO completedOrder(Integer orderId) {

            OrdersEntity order= orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderId)));
            OrdersDTO ordersDTO= mapToDTO(order);
            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime returnDay = LocalDateTime.now();
            String formattedNow = returnDay.format(formatter);
            if(ordersDTO.getOrderStatus().equals(OrderStatus.RENT) )
            {
                ordersDTO.setOrderStatus(OrderStatus.COMPLETED);
                ordersDTO.setReturnDate(formattedNow);
                orderRepository.save(mapToEntity(ordersDTO));
            }
            else if(ordersDTO.getOrderStatus().equals(OrderStatus.OVERDUE))
            {
                ordersDTO.setOrderStatus(OrderStatus.COMPLETED);
                ordersDTO.setReturnDate(formattedNow);
                //get overdue day
                LocalDateTime rentDay = LocalDateTime.parse(ordersDTO.getRentDate(), formatter);
                long numberDueDay= rentDay.until(returnDay, ChronoUnit.DAYS)-ordersDTO.getRentDayAmount();
             //subtract debit.

                Double fine= (ordersDTO.getTotalAmount()/ordersDTO.getRentDayAmount())*numberDueDay * 2;
                //update total amount
                if(fine>=5)
                {
                    fine=5.0;
                }
                ordersDTO.setTotalAmount(ordersDTO.getTotalAmount()+fine);
                orderRepository.save(mapToEntity(ordersDTO));
            }
            //update Book Detail Status
                 updateBookDetailStatus(ordersDTO);
        return ordersDTO;
    }



    //create orderDetail
    public List<OrderDetailEntity> createOrderDetail(List<Integer> bookIdList,Integer orderId)
    {
        List<OrderDetailEntity> books=new ArrayList<>();
        for (Integer bookId:bookIdList) {
            if(bookId!=null){
          OrderDetailEntity od=new OrderDetailEntity();
          od.setOrderId(orderId);
          od.setBookId(bookId);
          ODRepository.save(od);
            books.add(od);
              }

        }
        return books;
    }

    public void updateBookDetailStatus(OrdersDTO order)
    {
        for (OrderDetailDTO od: order.getOrderDetailsById())
        {
            if(!od.isLost())
            {
            BookDetailEntity bookDetail=bookDetailRepository.findById(od.getBookDetailId()).get();
            bookDetail.setStatus(BookDetailStatus.AVAILABLE.toString());
            bookDetailRepository.save(bookDetail);
            }
        }
    }
    public void updateBookDetailRent(OrdersDTO order)
    {
        for (OrderDetailDTO od: order.getOrderDetailsById())
        {
            if(!od.isLost())
            {
                BookDetailEntity bookDetail=bookDetailRepository.findById(od.getBookDetailId()).get();
                bookDetail.setStatus(BookDetailStatus.RENT.toString());
                bookDetailRepository.save(bookDetail);
            }
        }


    }

    //get rent amount
    public Double countTotalAmount(List<Integer> bookIdList,Integer numberOfDays)
    {
        double totalAmount=0.0;
        for (Integer bookId:bookIdList)
        {
            if(bookId!=null)
            {
                BookEntity myBook=new BookEntity();
               myBook= bookRepository.findById(bookId).orElse(null);
               if(myBook!=null)
               {
               totalAmount+= myBook.getPricePerDay()*numberOfDays;
                }
            }

        }
        return totalAmount;
    }


    public void changeBookDetailStatus(List<OrderDetailEntity> orderDetails)
    {
        for (OrderDetailEntity orderdetail:orderDetails)
        {
            BookEntity book=bookRepository.findById(orderdetail.getBookId()).orElseThrow(()->new ResourceNotFoundException("Book","id",String.valueOf(orderdetail.getBookId())));
                if(book!=null)
                {
                    //DOI STATUS
                    BookDetailEntity bdEntity=  book.getBookDetailsById().stream().filter(bd->bd.getStatus().equals(BookDetailStatus.AVAILABLE.toString())).findFirst().get();
                    bdEntity.setStatus(BookDetailStatus.BOOKED.toString());
                    //gan id cho bookdetail
                    orderdetail.setBookDetailId(bdEntity.getId());
                    ODRepository.save(orderdetail);
                    bookDetailRepository.save(bdEntity);
                }
        }
    }


    //CHECK STOCK

///
    public OrdersDTO mapToDTO(OrdersEntity order) {
        OrdersDTO ordersDTO = modelMapper.map(order, OrdersDTO.class);
        return ordersDTO;

    }

    public OrdersEntity mapToEntity(OrdersDTO ordersDTO) {
        OrdersEntity order = modelMapper.map(ordersDTO, OrdersEntity.class);
        return order;

    }
    public OrderDetailDTO mapODToDTO(OrderDetailEntity orderDetail) {
        OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
        return orderDetailDTO;

    }

    public OrderDetailEntity mapODToEntity(OrderDetailDTO ordersDDTO) {
        OrderDetailEntity orderDetail = modelMapper.map(ordersDDTO, OrderDetailEntity.class);
        return orderDetail;

    }
    public CustomerDTO mapCusToDTO(CustomerEntity customer) {
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;

    }

    public CustomerEntity mapCusToEntity(CustomerDTO customerDTO) {
        CustomerEntity customer = modelMapper.map(customerDTO, CustomerEntity.class);
        return customer;

    }



    public BookDetailDTO mapBookDetailToDTO(BookDetailEntity bookDetail) {
        BookDetailDTO bookDetailDTO = modelMapper.map(bookDetail, BookDetailDTO.class);
        return bookDetailDTO;

    }

    public BookDetailEntity mapBookDetailToEntity(BookDetailDTO bookDetailDTO) {
        BookDetailEntity bookDetail = modelMapper.map(bookDetailDTO, BookDetailEntity.class);
        return bookDetail;

    }
}
