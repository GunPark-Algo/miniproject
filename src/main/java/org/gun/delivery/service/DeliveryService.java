package org.gun.delivery.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gun.common.constants.MiniConstants;
import org.gun.common.vo.DeliveryVO;
import org.gun.data.model.Delivery;
import org.gun.data.model.Orders;
import org.gun.data.repos.DeliveryRepository;
import org.gun.data.repos.OrdersRepository;
import org.gun.order.controller.OrderController;
import org.gun.order.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService implements IDeliveryService {

    Logger logger = LogManager.getLogger(MenuService.class);

    @Autowired
    OrderController internalOrderController;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Override
    public DeliveryVO getDelivery(String orderNo) {

        Delivery delivery = deliveryRepository.findByOrderNo(orderNo);

        DeliveryVO responseDeliveryVO = new DeliveryVO();

        if (delivery != null) {
            responseDeliveryVO.setOrderNo(delivery.getOrderNo());
            responseDeliveryVO.setStatus(delivery.getStatus());
            logger.info("[배달 시스템] 배달 상태 변경 완료. orderNo : {} ", delivery.getOrderNo());
        }
        return responseDeliveryVO;
    }

    @Override
    public DeliveryVO submitDelivery(String orderNo) {

        Orders order = ordersRepository.findByOrderNo(orderNo);

        DeliveryVO responseDeliveryVO = new DeliveryVO();

        if(order==null) {
            return responseDeliveryVO;
        }

        Delivery delivery = new Delivery();
        delivery.setOrderNo(orderNo);
        delivery.setStatus(MiniConstants.ORDER_STATUS_RECEIPT);
        delivery.setDistance(order.getDistance());

        Delivery createdDelivery = deliveryRepository.save(delivery);
        logger.info("[배달 시스템] 배달 등록 완료. orderNo : {}, status : {}, distance : {}", delivery.getOrderNo(), delivery.getStatus(), delivery.getDistance());

        responseDeliveryVO.setOrderNo(createdDelivery.getOrderNo());
        responseDeliveryVO.setStatus(createdDelivery.getStatus());

        return responseDeliveryVO;
    }
}
