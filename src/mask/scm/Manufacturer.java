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
public class Manufacturer extends Company implements ISupplier {

    @Override
    public void setup() {
        super.setup();
        inventoryPeriods = 30;
        inventory = 3000;
    }

    public class ProductionPlan {

        private int produce() {
            return 100;
        }
    }
    private ProductionPlan productionPlan = new ProductionPlan();

    @Override
    protected int[] forecast(int periods) {
        int[] f = new int[periods];
        for (int i = 0; i < periods; i++) {
            f[i] = 100;
        }
        return f;
    }

    @Override
    public void productsReception() {
        productsReceived = productionPlan.produce();
        inventory += productsReceived;
    }

    @Override
    public void replenishment() {
        // ajust production plan if necessary
    }
}
