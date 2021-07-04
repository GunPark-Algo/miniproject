package org.gun.order.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.gun.common.constants.MiniConstants;
import org.gun.common.utils.JsonUtils;
import org.gun.common.vo.RequestMenuVO;
import org.gun.common.vo.RequestOrderVO;
import org.gun.common.vo.ResponseVO;
import org.gun.data.model.Menus;
import org.gun.data.model.Orders;
import org.gun.data.repos.MenusRepository;
import org.gun.data.repos.OrdersRepository;
import org.gun.delivery.controller.DeliveryController;
import org.gun.exception.MiniException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MenuService implements IMenuService {

    Logger logger = LogManager.getLogger(MenuService.class);

    @Autowired
    private DeliveryController deliveryController;

    @Autowired
    private MenusRepository menusRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @Override
    public List<Menus> findAll() {
        return (List<Menus>) menusRepository.findAll();
    }

    @Override
    public Menus getMenu(String menuCode) {
        return menusRepository.getById(menuCode);
    }

    @Override
    public void updateMenu(String menuCode, Integer orderCount) {
        menusRepository.submitQuantity(menuCode, orderCount);
    }

    @Override
    public ResponseVO getOrder(String orderNo) throws MiniException {
        ResponseVO orderResponse = new ResponseVO();
        Orders orders = ordersRepository.findByOrderNo(orderNo);

        if (orders != null) {
            logger.info("[주문 조회 성공] OrderNo : {}", orders.getOrderNo());
            orderResponse.setOrderNo(orders.getOrderNo());
            orderResponse.setShopNo(orders.getShopNo());
            orderResponse.setAcceptTime(orders.getAccessTime());
            orderResponse.setTotalPrice(orders.getTotalPrice());
            orderResponse.setResult(orders.getResult());
        } else {
            logger.error("[주문 조회 실패] 없는 주문 번호입니다");
            throw new MiniException("{\"errormsg\":\"없는 주문 번호입니다\"}");
        }
        return orderResponse;
    }

    @Override
    @Transactional
    public void setOrderStatus(String orderNo, String status) {
        Orders order = ordersRepository.findByOrderNo(orderNo);
        order.setResult(status);
        ordersRepository.save(order);
    }

    @Override
    @Transactional
    public ResponseVO submitOrder(RequestOrderVO requestOrder, String accessTime) throws MiniException {

        Set<String> checkDuplicateMenu = new HashSet<String>();

        Integer totalPrice = 0;

        Orders order = ordersRepository.findByOrderNo(requestOrder.getOrderNo());

        validateOfOrder(requestOrder, accessTime, checkDuplicateMenu, order);

        for (RequestMenuVO menuInOrder : requestOrder.getMenus()) {
            Menus menuForChecking = menusRepository.getById(menuInOrder.getMenuNo());
            totalPrice += menuForChecking.getPrice() * menuInOrder.getQuantity();
        }

        for (RequestMenuVO requestMenu : requestOrder.getMenus()) {
            menusRepository.submitQuantity(requestMenu.getMenuNo(), requestMenu.getQuantity());
        }

        Orders receiptOrder = makeSuccessOrders(requestOrder, accessTime, totalPrice);

        ordersRepository.save(receiptOrder);

        deliveryController.deliveryOrder(requestOrder.getOrderNo());

        logger.info("[주문] 등록 완료. orderNo : {}, distance : {}", receiptOrder.getOrderNo(), receiptOrder.getDistance());

        return makeSuccessResponse(requestOrder, totalPrice, accessTime);
    }

    private void validateOfOrder(RequestOrderVO requestOrder, String accessTime, Set<String> checkDuplicateMenu, Orders order) throws MiniException {
        if (order != null) {
            logger.error("[주문] 이미 존재. orderNo : {}", requestOrder.getOrderNo());
            makeMiniExceptionWithRespoon(requestOrder, accessTime);
        }

        if (isInvalidDistance(requestOrder.getDistance())) {
            logger.error("[주문] 불가능 거리. orderNo : {}, distance : {} ", requestOrder.getOrderNo(), requestOrder.getDistance());
            makeMiniExceptionWithRespoon(requestOrder, accessTime);
        }

        for (RequestMenuVO menuInOrder : requestOrder.getMenus()) {

            if (isInvalidQuantity(menuInOrder.getQuantity())) {
                logger.error("[주문] 불가능 수량. orderNo : {}, quantity : {}", requestOrder.getOrderNo(), menuInOrder.getQuantity());
                makeMiniExceptionWithRespoon(requestOrder, accessTime);
            }

            if (checkDuplicateMenu(checkDuplicateMenu, menuInOrder)) {
                logger.error("[주문] 중복 메뉴. orderNo : {}, menuCode : {}", requestOrder.getOrderNo(), menuInOrder.getMenuNo());
                makeMiniExceptionWithRespoon(requestOrder, accessTime);
            }

            Menus menuForChecking = menusRepository.getById(menuInOrder.getMenuNo());

            if (menuForChecking == null) {
                logger.error("[주문] 없는 메뉴. orderNo : {}, menuCode : {}", requestOrder.getOrderNo(), menuInOrder.getMenuNo());
                makeMiniExceptionWithRespoon(requestOrder, accessTime);
            }

            if (menuForChecking.getQuantity() < menuInOrder.getQuantity()) {
                logger.error("[주문] 수량 부족. orderNo : {}, menuCode : {}, remainCount : {}, orderCount : {}", requestOrder.getOrderNo(), menuInOrder.getMenuNo(), menuForChecking.getQuantity(), menuInOrder.getQuantity());
                makeMiniExceptionWithRespoon(requestOrder, accessTime);
            }

            checkDuplicateMenu.add(menuInOrder.getMenuNo());
        }
    }

    private ResponseVO makeMiniExceptionWithRespoon(RequestOrderVO requestOrder, String accessTime) throws MiniException {
        ResponseVO errorResVO = makeCancelResponse(requestOrder, accessTime);
        throw new MiniException(JsonUtils.writeValueAsString(errorResVO));
    }


    private Orders makeSuccessOrders(RequestOrderVO requestOrder, String accessTime, Integer totalPrice) {
        Orders order = new Orders();
        order.setOrderNo(requestOrder.getOrderNo());
        order.setShopNo(requestOrder.getShopNo());
        order.setTotalPrice(totalPrice);
        order.setDistance(requestOrder.getDistance());
        order.setAccessTime(accessTime);
        order.setResult(MiniConstants.ORDER_STATUS_RECEIPT);
        return order;
    }

    private Orders makeCancelOrders(RequestOrderVO requestOrder, String accessTime) {
        Orders order = new Orders();
        order.setOrderNo(requestOrder.getOrderNo());
        order.setShopNo(requestOrder.getShopNo());
        order.setTotalPrice(0);
        order.setDistance(requestOrder.getDistance());
        order.setAccessTime(accessTime);
        order.setResult(MiniConstants.ORDER_STATUS_CANCEL);
        return order;
    }

    private boolean isInvalidDistance(Integer distance) {
        if (distance < MiniConstants.ORDER_DISTANCE_MIN || distance > MiniConstants.ORDER_DISTANCE_MAX) {
            return true;
        }
        if (distance % MiniConstants.ORDER_DISTANCE_UNIT != 0) {
            return true;
        }
        return false;
    }

    private boolean isInvalidQuantity(Integer quantity) {
        if (quantity < 0 || quantity > 5) {
            return true;
        }
        return false;
    }


    private boolean checkDuplicateMenu(Set<String> checkDuplicateMenu, RequestMenuVO menuInOrder) {
        if (checkDuplicateMenu.contains(menuInOrder.getMenuNo())) {
            return true;
        }
        return false;
    }

    private ResponseVO makeSuccessResponse(RequestOrderVO requestOrder, Integer totalPrice, String accessTime) {
        ResponseVO successResponse = new ResponseVO();
        successResponse.setOrderNo(requestOrder.getOrderNo());
        successResponse.setShopNo(requestOrder.getShopNo());
        successResponse.setAcceptTime(accessTime);
        successResponse.setTotalPrice(totalPrice);
        successResponse.setResult(MiniConstants.ORDER_STATUS_RECEIPT);

        return successResponse;
    }

    private ResponseVO makeCancelResponse(RequestOrderVO order, String accessTime) {
        ResponseVO cancelResponse = new ResponseVO();

        cancelResponse.setOrderNo(order.getOrderNo());
        cancelResponse.setShopNo(order.getShopNo());
        cancelResponse.setAcceptTime(accessTime);
        cancelResponse.setTotalPrice(0);
        cancelResponse.setResult(MiniConstants.ORDER_STATUS_CANCEL);

        return cancelResponse;
    }


}
