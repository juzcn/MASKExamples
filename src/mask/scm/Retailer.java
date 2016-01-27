/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

/**
 *
 * @author zj
 */
public class Retailer extends Company implements IBuyer {

    @Override
    public void setup() {
        super.setup();
        inventoryPeriods = 10;
        inventory = 1000;
    }

    @Override
    protected int[] forecast(int periods) {
        int[] f = new int[periods];
        for (int i = 0; i < periods; i++) {
            f[i] = 100;
        }
        return f;
    }

    private class OrderGenerator {

        private int generate() {
            return 100;
        }
    }
    private final OrderGenerator orderGenerator = new OrderGenerator();

    @Override
    public boolean periodBegin() {
        productsReception();
        ordersProcessing();
        replenishment();
        return true;
    }

    @Override
    public boolean ordersProcessing() {
        totalOrderQuantity = orderGenerator.generate();
        if (inventory >= totalOrderQuantity) {
            inventory -= totalOrderQuantity;
        } else {
            totalStockout = totalOrderQuantity - inventory;
            inventory = 0;
        }
        return true;
    }
}
