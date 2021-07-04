package org.gun.order.service;

import org.gun.data.model.Menus;
import org.gun.common.vo.ResponseVO;
import org.gun.common.vo.RequestOrderVO;
import org.gun.exception.MiniException;

import java.util.List;

public interface IMenuService {
    List<Menus> findAll();
    Menus getMenu(String menuId);
    void updateMenu(String menuId, Integer orderCount);
    ResponseVO submitOrder(RequestOrderVO order, String time) throws MiniException;
    void setOrderStatus(String orderNo, String status);
    ResponseVO getOrder(String orderNo) throws MiniException;
}
