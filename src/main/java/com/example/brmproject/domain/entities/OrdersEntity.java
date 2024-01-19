package com.example.brmproject.domain.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Collection;
import java.util.Objects;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders", schema = "brmproject", catalog = "")
public class OrdersEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "rent_date", nullable = true, length = 45)
    private String rentDate;
    @Basic
    @Column(name = "booking_date", nullable = true, length = 45)
    private String bookingDate;
    @Basic
    @Column(name = "order_status", nullable = true, length = 45)
    private String orderStatus;
    @Basic
    @Column(name = "return_date", nullable = true, length = 45)
    private String returnDate;
    @Basic
    @Column(name = "rent_day_amount", nullable = true)
    private Integer rentDayAmount;
    @Basic
    @Column(name = "total_amount", nullable = true, precision = 2)
    private Double totalAmount;
    @Basic
    @Column(name = "employee_id", nullable = true)
    private Integer employeeId;
    @Basic
    @Column(name = "customer_id", nullable = true)
    private Integer customerId;
    @OneToMany(mappedBy = "ordersByOrderId",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.EAGER)
    private Collection<OrderDetailEntity> orderDetailsById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", insertable = false, updatable = false)
    private StaffEntity staffByEmployeeId;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CustomerEntity customerByCustomerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrdersEntity that = (OrdersEntity) o;
        return id == that.id && Objects.equals(rentDate, that.rentDate) && Objects.equals(bookingDate, that.bookingDate) && Objects.equals(orderStatus, that.orderStatus) && Objects.equals(returnDate, that.returnDate) && Objects.equals(rentDayAmount, that.rentDayAmount) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(employeeId, that.employeeId) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rentDate, bookingDate, orderStatus, returnDate, rentDayAmount, totalAmount, employeeId, customerId);
    }

    public Collection<OrderDetailEntity> getOrderDetailsById() {
        return orderDetailsById;
    }

    public void setOrderDetailsById(Collection<OrderDetailEntity> orderDetailsById) {
        this.orderDetailsById = orderDetailsById;
    }

    public StaffEntity getStaffByEmployeeId() {
        return staffByEmployeeId;
    }

    public void setStaffByEmployeeId(StaffEntity staffByEmployeeId) {
        this.staffByEmployeeId = staffByEmployeeId;
    }

    public CustomerEntity getCustomerByCustomerId() {
        return customerByCustomerId;
    }

    public void setCustomerByCustomerId(CustomerEntity customerByCustomerId) {
        this.customerByCustomerId = customerByCustomerId;
    }
}
