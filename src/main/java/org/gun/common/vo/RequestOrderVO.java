package org.gun.common.vo;

import java.util.List;


public class RequestOrderVO {

    private String orderNo;

    private String shopNo;

    private Integer distance;

    private List<RequestMenuVO> menus;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getShopNo() {
        return shopNo;
    }

    public void setShopNo(String shopNo) {
        this.shopNo = shopNo;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<RequestMenuVO> getMenus() {
        return menus;
    }

    public void setMenus(List<RequestMenuVO> menus) {
        this.menus = menus;
    }
}
