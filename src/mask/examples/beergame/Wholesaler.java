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
public class Wholesaler extends Company {

    @Override
    public void setup() {
        super.setup();
        inventory = 300;
        deliveryNotices.offer(new DeliveryNotice(1, 100));
        deliveryNotices.offer(new DeliveryNotice(2, 100));
        deliveryNotices.offer(new DeliveryNotice(3, 100));
        deliveryNotices.offer(new DeliveryNotice(4, 100));

    }

}
