package org.gun.delivery.service;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.gun.common.constants.MiniConstants;
import org.gun.common.vo.DeliveryVO;
import org.gun.data.model.Delivery;
import org.gun.data.model.Orders;
import org.gun.data.repos.DeliveryRepository;
import org.gun.data.repos.OrdersRepository;
import org.gun.exception.MiniException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryServiceTest {
    public static final Long TEST_DELIVERY_ID = 1L;
    public static final String TEST_ORDER_NO = "orderNo1";
    private static final String TEST_SHOP_NO = "SHOP_01";
    private static final Integer TEST_DISTANCE = 500;

    @InjectMocks
    DeliveryService deliveryService;

    @Mock
    OrdersRepository ordersRepository;

    @Mock
    DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("getDelivery 성공 시나리오 테스트")
    public void test_getDelivery_success() throws MiniException {

        Delivery delivery = makeDeliveryForTest();

        DeliveryVO expected = makeDeliveryVOForTest(delivery);

        when(deliveryRepository.findByOrderNo(TEST_ORDER_NO)).thenReturn(delivery);

        DeliveryVO actual = deliveryService.getDelivery(TEST_ORDER_NO);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("submitDelivery 성공 시나리오 테스트")
    public void test_submitDelivery_success() throws MiniException {

        Orders order = makeOrderForTest();

        Delivery delivery = new Delivery();
        delivery.setOrderNo(order.getOrderNo());
        delivery.setStatus(MiniConstants.ORDER_STATUS_RECEIPT);
        delivery.setDistance(order.getDistance());

        when(ordersRepository.findByOrderNo(TEST_ORDER_NO)).thenReturn(order);
        doReturn(delivery).when(deliveryRepository).save(any());

        DeliveryVO expected = new DeliveryVO();
        expected.setOrderNo(order.getOrderNo());
        expected.setStatus(MiniConstants.ORDER_STATUS_RECEIPT);

        DeliveryVO actual = deliveryService.submitDelivery(TEST_ORDER_NO);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    private Orders makeOrderForTest() {
        Orders order = new Orders();

        order.setOrderNo(TEST_ORDER_NO);
        order.setShopNo(TEST_SHOP_NO);
        order.setAccessTime("testtime1");
        order.setTotalPrice(10000);
        order.setResult("testresult");

        return order;
    }

    private DeliveryVO makeDeliveryVOForTest(Delivery delivery) {
        DeliveryVO testDeliveryVO = new DeliveryVO();

        testDeliveryVO.setOrderNo(delivery.getOrderNo());
        testDeliveryVO.setStatus(delivery.getStatus());

        return testDeliveryVO;
    }

    private Delivery makeDeliveryForTest() {
        Delivery testDelivery = new Delivery();
        testDelivery.setId(TEST_DELIVERY_ID);
        testDelivery.setStatus(MiniConstants.ORDER_STATUS_RECEIPT);
        testDelivery.setDistance(TEST_DISTANCE);
        testDelivery.setOrderNo(TEST_ORDER_NO);

        return testDelivery;
    }
}
