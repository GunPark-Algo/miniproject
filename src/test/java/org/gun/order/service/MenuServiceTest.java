package org.gun.order.service;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.gun.common.constants.MiniConstants;
import org.gun.common.vo.DeliveryVO;
import org.gun.common.vo.RequestMenuVO;
import org.gun.common.vo.RequestOrderVO;
import org.gun.common.vo.ResponseVO;
import org.gun.data.model.Menus;
import org.gun.data.model.Orders;
import org.gun.data.repos.MenusRepository;
import org.gun.data.repos.OrdersRepository;
import org.gun.delivery.controller.DeliveryController;
import org.gun.exception.MiniException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    public static final String TEST_ORDER_NO = "orderNo1";
    private static final String TEST_ORDER_ACCESSTIME = "100000000";
    private static final String TEST_SHOP_NO = "SHOP_01";
    private static final Integer TEST_DISTANCE = 500;
    private static final String TEST_MENU_NO = "MENU_01";
    private static final Integer TEST_MENU_COUNT = 1;
    private static final Integer TEST_MENU_PRICE = 5000;

    @InjectMocks
    private MenuService menuService;

    @Mock
    private MenusRepository menusRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private DeliveryController deliveryController;

    @Test
    @DisplayName("getOrder 성공 시나리오 테스트")
    public void test_getOrder_success() throws MiniException {
        String testOrderNo = TEST_ORDER_NO;
        Orders orders = makeOrderForTest();

        ResponseVO expected = makeResponseVOForTest(orders);

        when(ordersRepository.findByOrderNo(testOrderNo)).thenReturn(orders);

        ResponseVO actual = menuService.getOrder(testOrderNo);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }


    @Test
    @DisplayName("SetOrder 성공 시나리오 테스트")
    public void test_submitOrder_success() throws MiniException {
        RequestOrderVO requestOrder = makeRequestOrderForTest();
        Orders order = makeOrderForTest();
        Menus menus = makeMenusForTest();

        ResponseVO expected = makeSuccessResponseVOForTest(requestOrder);

        when(ordersRepository.findByOrderNo(requestOrder.getOrderNo())).thenReturn(null);
        when(menusRepository.getById(requestOrder.getMenus().get(0).getMenuNo())).thenReturn(menus);
        doNothing().when(menusRepository).submitQuantity(any(), any());
        doReturn(new Orders()).when(ordersRepository).save(any());
        doReturn(new DeliveryVO()).when(deliveryController).deliveryOrder(requestOrder.getOrderNo());

        ResponseVO actual = menuService.submitOrder(requestOrder, TEST_ORDER_ACCESSTIME);

        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }



    private RequestOrderVO makeRequestOrderForTest() {
        RequestOrderVO requestOrderVO= new RequestOrderVO();
        requestOrderVO.setOrderNo(TEST_ORDER_NO);
        requestOrderVO.setOrderNo(TEST_SHOP_NO);
        requestOrderVO.setDistance(TEST_DISTANCE);
        List<RequestMenuVO> list = new ArrayList<>();
        RequestMenuVO requestMenuVO = new RequestMenuVO();
        requestMenuVO.setMenuNo(TEST_MENU_NO);
        requestMenuVO.setQuantity(TEST_MENU_COUNT);
        list.add(requestMenuVO);

        requestOrderVO.setMenus(list);

        return requestOrderVO;
    }

    private ResponseVO makeSuccessResponseVOForTest(RequestOrderVO requestOrder) {
        ResponseVO successResponse = new ResponseVO();
        successResponse.setOrderNo(requestOrder.getOrderNo());
        successResponse.setShopNo(requestOrder.getShopNo());
        successResponse.setAcceptTime(TEST_ORDER_ACCESSTIME);
        successResponse.setTotalPrice(TEST_MENU_PRICE);
        successResponse.setResult(MiniConstants.ORDER_STATUS_RECEIPT);

        return successResponse;
    }

    private ResponseVO makeResponseVOForTest(Orders orders) {
        ResponseVO Expected = new ResponseVO();
        Expected.setOrderNo(orders.getOrderNo());
        Expected.setShopNo(orders.getShopNo());
        Expected.setAcceptTime(orders.getAccessTime());
        Expected.setTotalPrice(orders.getTotalPrice());
        Expected.setResult(orders.getResult());
        return Expected;
    }

    private Menus makeMenusForTest() {
        Menus menu = new Menus();

        menu.setCode(TEST_MENU_NO);
        menu.setQuantity(TEST_MENU_COUNT);
        menu.setName("menuname");
        menu.setPrice(TEST_MENU_PRICE);

        return menu;
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

}
