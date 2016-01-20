/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame;

/**
 *
 * @author zj
 */
public class Retailer extends Company {

    private int periodOrder = 10;

    @Override
    public void setup() {
        super.setup();
        deliveryNotices.offer(new DeliveryNotice(1, 10));
        deliveryNotices.offer(new DeliveryNotice(2, 10));
        deliveryNotices.offer(new DeliveryNotice(3, 10));
        deliveryNotices.offer(new DeliveryNotice(4, 10));
    }

    @Override
    public boolean periodBegin() {
        totalOrderQuantity = 0;
        orderCounter = 0;
        processDeliveries();
        processOrder(periodOrder);
        return replenishment();
    }

}
