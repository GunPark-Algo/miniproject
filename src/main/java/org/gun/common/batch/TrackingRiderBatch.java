package org.gun.common.batch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gun.common.constants.MiniConstants;
import org.gun.data.model.Delivery;
import org.gun.data.model.Riders;
import org.gun.data.repos.DeliveryRepository;
import org.gun.data.repos.OrdersRepository;
import org.gun.data.repos.RidersRepository;
import org.gun.order.controller.OrderController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
public class TrackingRiderBatch {

    Logger logger = LogManager.getLogger(TrackingRiderBatch.class);

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    RidersRepository ridersRepository;

    @Autowired
    OrderController orderController;

    @Scheduled(fixedRate = 10000)
    public void trackingRider() {
        List<Riders> riders = ridersRepository.findAll();

        if (riders != null && riders.size() > 0) {
            for (Riders rider : riders) {
                if (rider.getDeliveryId() != 0) {

                    int speed = rider.getSpeed();
                    //??? null ???
                    int remainDistance = rider.getRemainDistance();
                    remainDistance -= speed;

                    if (remainDistance <= 0) {

                        long deliveryId = rider.getDeliveryId();
                        Delivery delivery = deliveryRepository.getById(deliveryId);

                        //오더 서비스에 Complete 알림
                        orderController.setOrder(delivery.getOrderNo(), MiniConstants.ORDER_STATUS_COMPLETE);

                        //delivery 상태 Complete
                        delivery.setStatus(MiniConstants.ORDER_STATUS_COMPLETE);
                        deliveryRepository.save(delivery);

                        rider.setRemainDistance(null);
                        rider.setDeliveryId(0L);
                        ridersRepository.save(rider);

                        logger.info("[라이더 트래킹] 라이더 배달 완료. 라이더 : {}", rider.getRiderId());
                    } else {
                        rider.setRemainDistance(remainDistance);
                        ridersRepository.save(rider);
                        logger.info("[라이더 트래킹] 라이더 : {}, 배달ID : {}, 남은거리 : {}", rider.getRiderId(), rider.getDeliveryId(), remainDistance);
                    }
                } else {
                    logger.info("[라이더 트래킹] 라이더 : {}, 상태 : {}", rider.getRiderId(), rider.getDeliveryId());
                }
            }
        }

    }

    @Scheduled(fixedRate = 10000)
    public void pickupOrderToRider() {

        List<Delivery> Deliveries = deliveryRepository.findReceiptStatus();

        if (Deliveries != null && Deliveries.size() > 0) {
            for (Delivery delivery : Deliveries) {
                if (delivery.getStatus().equals(MiniConstants.ORDER_STATUS_RECEIPT)) {

                    List<Riders> noWorkRiders = ridersRepository.findNoWorkRider();
                    if (noWorkRiders != null && noWorkRiders.size() > 0) {

                        //오더 서비스에 Pickup 호출
                        orderController.setOrder(delivery.getOrderNo(), MiniConstants.ORDER_STATUS_PICKUP);

                        //delivery 상태 pickup
                        delivery.setStatus(MiniConstants.ORDER_STATUS_PICKUP);
                        deliveryRepository.save(delivery);

                        Riders rider = noWorkRiders.get(0);
                        rider.setDeliveryId(delivery.getId());
                        rider.setRemainDistance(delivery.getDistance());
                        ridersRepository.save(rider);

                        logger.info("[라이더 주문 선정] 라이더 : {}, 주문번호 : {}, 배달ID : {}, 배달수단 : {} 배달 시작합니다", rider.getRiderId(), delivery.getOrderNo(), delivery.getId(), rider.getVehicle());
                    } else {
                        logger.info("[라이더 주문 선정] 모든 라이더가 배달 중");
                    }
                }
            }
        } else {
            logger.info("[라이더 주문 선정] 배달 건이 없습니다");
        }
    }
}
