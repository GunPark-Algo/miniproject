package org.gun.order.controller;

import org.gun.exception.MiniException;
import org.gun.order.service.IMenuService;
import org.gun.common.utils.Utils;
import org.gun.common.vo.RequestOrderVO;
import org.gun.common.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

@Controller
public class OrderController {

    @Autowired
    private IMenuService menuService;

    @GetMapping("/order")
    public @ResponseBody
    ResponseVO getOrder(@RequestParam String orderNo) throws MiniException {
        return menuService.getOrder(orderNo);
    }

    @PutMapping("/order")
    public @ResponseBody
    ResponseVO orderMenus(@RequestBody RequestOrderVO order, ModelMap model) throws MiniException {
        model.addAttribute(order);

        String accessTime = Utils.getCurrentTimeString();

        return menuService.submitOrder(order, accessTime);
    }

    @PutMapping("/deliveryOrder")
    public String setOrder(@RequestParam String orderNo, String status) {

        menuService.setOrderStatus(orderNo, status);

        return orderNo;
    }

}
