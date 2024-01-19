package com.example.brmproject.service.imp;

import com.example.brmproject.domain.dto.BookDetailDTO;
import com.example.brmproject.domain.dto.OrderDetailDTO;
import com.example.brmproject.domain.entities.BookDetailEntity;
import com.example.brmproject.domain.entities.CustomerEntity;
import com.example.brmproject.domain.entities.OrderDetailEntity;
import com.example.brmproject.domain.entities.OrdersEntity;
import com.example.brmproject.exception.ResourceNotFoundException;
import com.example.brmproject.exception.order.OrderNotFoundException;
import com.example.brmproject.repositories.BookDetailEntityRepository;
import com.example.brmproject.repositories.CustomerEntityRepository;
import com.example.brmproject.repositories.OrderDetailEntityRepository;
import com.example.brmproject.repositories.OrdersEntityRepository;
import com.example.brmproject.service.OrderDetailService;
import com.example.brmproject.ultilities.SD.BookDetailStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderDetailServiceImp implements OrderDetailService {
    private OrderDetailEntityRepository ODRepository;
    private OrdersEntityRepository orderRepository;
    private BookDetailEntityRepository bdRepository;
    private CustomerEntityRepository cusRepo;
    private ModelMapper modelMapper;

    @Autowired
    public OrderDetailServiceImp(OrderDetailEntityRepository ODRepository,
                                 BookDetailEntityRepository bdRepository,
                                 CustomerEntityRepository cusRepo,
                                 ModelMapper modelMapper,
                                 OrdersEntityRepository orderRepository
    ) {
        this.ODRepository = ODRepository;
        this.bdRepository = bdRepository;
        this.cusRepo = cusRepo;
        this.modelMapper = modelMapper;
        this.orderRepository = orderRepository;
    }


    @Override
    public OrderDetailDTO createOrderDetail(Integer bookId, Integer orderId) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setBookId(bookId);
        orderDetailDTO.setOrderId(orderId);
        return null;
    }

    @Override
    public OrderDetailDTO markAsLost(Integer orderDetailId) {
        OrderDetailEntity orderDetail = ODRepository.findById(orderDetailId).orElseThrow(() -> new ResourceNotFoundException("Order Detail", "id", String.valueOf(orderDetailId)));
        OrderDetailDTO odDTO = mapToDTO(orderDetail);
        BookDetailDTO bookDetail = odDTO.getBookByBookId()
                .getBookDetailsById().stream()
                .filter(bd -> odDTO.getBookDetailId() == bd.getId()).findAny().orElse(null);
        if (bookDetail != null) {
            bookDetail.setStatus(BookDetailStatus.LOST.toString());
            orderDetail.setLost(true);
            ODRepository.save(orderDetail);
            bdRepository.save(mapBDToEntity(bookDetail));
            CustomerEntity customer = orderDetail.getOrdersByOrderId().getCustomerByCustomerId();
            //get fine
            Double fine = orderDetail.getBookByBookId().getPricePerDay() * 20;
            Double debit = customer.getDebit();
            if (fine >= 15) {
                fine = 15.0;
            }
            debit -= fine;
            customer.setDebit(debit);
            cusRepo.save(customer);
            return mapToDTO(orderDetail);
        }
        return null;
    }

    @Override
    public List<OrderDetailDTO> getOrdersDetail(int orderId, int userId) {
        Optional<OrdersEntity> foundOrder = orderRepository.findByIdAndCustomerId(orderId, userId);

        if (foundOrder.isEmpty()) {
            throw new OrderNotFoundException("Order not found");
        }

        List<OrderDetailEntity> orderDetailList = ODRepository.findByOrderId(foundOrder.get().getId());
        return orderDetailList.stream().map(order -> mapToDTO(order)).collect(Collectors.toList());
    }


    public OrderDetailDTO mapToDTO(OrderDetailEntity orderDetail) {
        OrderDetailDTO orderDetailDTO = modelMapper.map(orderDetail, OrderDetailDTO.class);
        return orderDetailDTO;

    }

    public OrderDetailEntity mapToEntity(OrderDetailDTO orderDetailDTO) {
        OrderDetailEntity orderDetail = modelMapper.map(orderDetailDTO, OrderDetailEntity.class);
        return orderDetail;

    }

    public BookDetailDTO mapBDToDTO(BookDetailEntity bookDetail) {
        BookDetailDTO bookDetailDTO = modelMapper.map(bookDetail, BookDetailDTO.class);
        return bookDetailDTO;

    }

    public BookDetailEntity mapBDToEntity(BookDetailDTO bookDetailDTO) {
        BookDetailEntity bookDetail = modelMapper.map(bookDetailDTO, BookDetailEntity.class);
        return bookDetail;

    }

}
