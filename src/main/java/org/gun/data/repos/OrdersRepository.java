package org.gun.data.repos;

import org.gun.data.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @Query("select o from Orders o where o.orderNo = ?1")
    Orders findByOrderNo(String orderNo);
}
