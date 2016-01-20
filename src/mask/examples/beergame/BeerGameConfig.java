/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame;

import mask.executor.MKExecutor;
import mask.rununit.RunGroup;
import mask.executor.LocalConfig;

/**
 *
 * @author zj
 */
public class BeerGameConfig extends LocalConfig<BeerGameLocal> {

    private Manufacturer manufacturer;
    private Wholesaler wholesaler;
    private Retailer[] retailers;

    public BeerGameConfig() {
        super(BeerGameLocal.class);
    }

    @Override
    public void setup() {
        wholesaler.setWholesaler(manufacturer);
        wholesaler.setCustomers(retailers);
        manufacturer.setCustomers(wholesaler);
        manufacturer.setWholesaler((Wholesaler) null);
        for (int i = 0;
                i < 10; i++) {
            retailers[i].setWholesaler(wholesaler);
            retailers[i].setCustomers((Company[]) null);
        }
    }

    public static void main(String args[]) {
        MKExecutor.newLocalExecutor(new BeerGameConfig(), null, new BeerGameXSLLogger(), new BeerGameCSVLogger()).run();
    }

    @Override
    public RunGroup createContainer() {

        RunGroup container = RunGroup.newThreadGroup();

        manufacturer = new Manufacturer();
        wholesaler = new Wholesaler();
        RunGroup retailerGroup = RunGroup.newThreadGroup();
        retailers = new Retailer[10];
        for (int i = 0;
                i < 10; i++) {
            retailers[i] = new Retailer();
        }

        retailerGroup.addAll(retailers);

        container.addAll(manufacturer, wholesaler, retailerGroup);
        return container;
    }

}
