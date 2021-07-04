package org.gun.data.repos;

import org.gun.common.constants.MiniConstants;
import org.gun.data.model.Delivery;
import org.gun.data.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    @Query("select d from Delivery d where d.orderNo = ?1")
    Delivery findByOrderNo(String orderNo);

    @Query("select d from Delivery d where d.status = 'RECEIPT'")
    List<Delivery> findReceiptStatus();

}
