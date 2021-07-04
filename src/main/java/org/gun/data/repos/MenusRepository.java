package org.gun.data.repos;

import org.gun.data.model.Menus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MenusRepository extends JpaRepository<Menus, String> {
    @Modifying
    @Query("update Menus m set m.quantity = m.quantity - ?2 where m.code = ?1")
    void submitQuantity(String code, Integer quantity);
}
