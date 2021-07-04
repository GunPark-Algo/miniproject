package org.gun.delivery.controller;

import org.gun.common.vo.DeliveryVO;
import org.gun.delivery.service.IDeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeliveryController {

    @Autowired
    private IDeliveryService deliveryService;

    @GetMapping("/delivery")
    public @ResponseBody
    DeliveryVO getDelivery(@RequestParam String orderNo) {
        DeliveryVO responseDeliveryVO = deliveryService.getDelivery(orderNo);

        return responseDeliveryVO;
    }

    @PutMapping("/delivery")
    public @ResponseBody
    DeliveryVO deliveryOrder(@RequestParam String orderNo) {
        return deliveryService.submitDelivery(orderNo);
    }
}
