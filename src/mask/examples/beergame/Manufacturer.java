package mask.examples.beergame;

import mask.logging.LabelStr;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zj
 */
@LabelStr("制造商")
public class Manufacturer extends Company {

    @Override
    protected boolean replenishment() {
        toOrder = 4 * (totalOrderQuantity) - inventory;
//        System.out.println(this + "  products to produce = " + toOrder + " at period " + time());
        deliveryNotices.add(new DeliveryNotice(time() + 4, toOrder));
        timerCondition.setAtTime(time() + 1);
        state=CompanyState.Ready;
        return true;
    }

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
