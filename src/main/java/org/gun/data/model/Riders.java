package org.gun.data.model;

import javax.persistence.*;

@Entity
@Table(indexes = @Index(name = "i_riders", columnList = "riderId"))
public class Riders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column
    String riderId;

    @Column
    String riderName;

    @Column
    String vehicle;

    @Column
    Integer speed;

    @Column
    Long deliveryId;

    @Column
    Integer remainDistance;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public Integer getRemainDistance() {
        return remainDistance;
    }

    public void setRemainDistance(Integer remainDistance) {
        this.remainDistance = remainDistance;
    }
}
