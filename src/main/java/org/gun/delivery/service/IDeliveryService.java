package org.gun.delivery.service;

import org.gun.common.vo.DeliveryVO;

public interface IDeliveryService {
    DeliveryVO getDelivery(String orderNo);

    DeliveryVO submitDelivery(String orderNo);
}
