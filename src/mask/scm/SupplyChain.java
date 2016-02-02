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

    Retailer retailer;
    Manufacturer manufacturer;
    Wholesaler wholesaler;

    @Override
    public RunGroup createContainer() {
        RunGroup container = RunGroup.newThreadGroup();
        retailer = new Retailer();
        wholesaler = new Wholesaler();
        manufacturer = new Manufacturer();

        container.addAll(retailer, manufacturer, wholesaler);
        return container;
    }

    @Override
    public void setup() {
        Company.contract(retailer, wholesaler, 3);
        Company.contract(wholesaler, manufacturer, 9);
    }

    public static void main(String args[]) {
        SupplyChain sc = new SupplyChain();
        sc.disableLogging();
        LocalExecutor executor = MasterExecutor.newLocalExecutor(sc);
        executor.threadStart(50);

    }
}
