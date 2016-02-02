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
public class Wholesaler extends Company implements IBuyer, ISupplier {

    @Override
    public void setup() {
        super.setup();
        inventoryPeriods = 20;
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

}
