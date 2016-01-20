/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame.distributed;

import mask.examples.beergame.Manufacturer;
import mask.examples.beergame.Wholesaler;
import mask.rununit.RunGroup;
import mask.service.StringFilter;
import java.io.Serializable;
import mask.executor.MKExecutor;
import mask.executor.RemoteConfig;

/**
 *
 * @author zj
 */
public class Remote1Config extends RemoteConfig {

    private Manufacturer manufacturer;
    private Wholesaler wholesaler;

    public Remote1Config() {
        super("Remote1");
    }

    @Override
    public void setup() {
        wholesaler.setWholesaler(manufacturer);
        Integer[] retailers = service().lookupAgents((StringFilter & Serializable) ((s) -> s.contains("Retailer")));
        wholesaler.setCustomers(retailers);
        manufacturer.setCustomers(wholesaler);
        manufacturer.setWholesaler((Wholesaler) null);
    }

    public static void main(String args[]) {
        MKExecutor.newRemoteExecutor("Remote1").run();
    }

    @Override
    public RunGroup createContainer() {
        RunGroup container = RunGroup.newLoopGroup();

        manufacturer = new Manufacturer();
        wholesaler = new Wholesaler();
        container.addAll(manufacturer, wholesaler);
        return container;
    }

}
