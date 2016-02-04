/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

import java.io.Serializable;

/**
 *
 * @author zj
 */
public class Manufacturer extends Company implements ISupplier {

    @Override
    public void setup() {
        super.setup();
        inventoryPeriods = 30;
        inventory = 10000000;
    }

    public class ProductionPlan implements Serializable {

        private int produce() {
            return 1000000;
        }
    }
    private final ProductionPlan productionPlan = new ProductionPlan();

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
        received = productionPlan.produce();
        inventory += received;
    }

    @Override
    public void replenishment() {
        // ajust production plan if necessary
        state = State.WaitOrders;
    }
}
