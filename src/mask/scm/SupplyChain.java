/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

import mask.executor.LocalExecutor;
import mask.executor.LocalModel;
import mask.executor.MasterExecutor;
import mask.rununit.RunGroup;

/**
 *
 * @author zj
 */
public class SupplyChain extends LocalModel {

    Retailer[] retailers;
    Manufacturer manufacturer;
    Wholesaler[] wholesalers;

    @Override
    public RunGroup createContainer() {
        RunGroup container = RunGroup.newThreadPoolGroup(10);
        retailers = new Retailer[10000];
        for (int i = 0; i < 10000; i++) {
            retailers[i] = new Retailer();
        }
        wholesalers = new Wholesaler[100];
        for (int i = 0; i < 100; i++) {
            wholesalers[i] = new Wholesaler();
        }
        manufacturer = new Manufacturer();

        container.addAll(retailers);
        container.addAll(wholesalers);
        container.add(manufacturer);
        return container;
    }

    @Override
    public void setup() {
        for (int i = 0; i < 100; i++) {
            Company.contract(wholesalers[i], manufacturer, 9);
        }
        for (int i = 0; i < 10000; i++) {
            Company.contract(retailers[i], wholesalers[i / 100], 3);
        }
    }

    public static void main(String args[]) {
        SupplyChain sc = new SupplyChain();
        sc.disableLogging();
        LocalExecutor executor = MasterExecutor.newLocalExecutor(sc);
        executor.threadStart(50);

    }
}
