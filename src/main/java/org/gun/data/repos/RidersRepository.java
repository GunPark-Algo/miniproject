package org.gun.data.repos;

import org.gun.data.model.Menus;
import org.gun.data.model.Orders;
import org.gun.data.model.Riders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RidersRepository extends JpaRepository<Riders, Long> {
    @Query("select r from Riders r where r.riderId = ?1")
    Riders findByRiderId(String riderId);

    @Query("select r from Riders r where r.deliveryId = 0L")
    List<Riders> findNoWorkRider();
}
