/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame.distributed;

import mask.examples.beergame.Company;
import mask.examples.beergame.Retailer;
import mask.executor.LocalExecutor;
import mask.rununit.RunGroup;
import mask.service.StringFilter;
import java.io.Serializable;
import mask.executor.RemoteConfig;

/**
 *
 * @author zj
 */
public class Remote2Config extends RemoteConfig {

    private Retailer[] retailers;

    public Remote2Config() {
        super("Remote2");
    }

    @Override
    public void setup() {
        Integer[] wholesalers = service().lookupAgents((StringFilter & Serializable) ((s) -> s.contains("Wholesaler")));
        for (int i = 0;
                i < 10; i++) {
            retailers[i].setWholesaler(wholesalers[0]);
            retailers[i].setCustomers((Company[]) null);
        }
    }

    public static void main(String args[]) {
        LocalExecutor.newRemoteExecutor("Remote2").run();
    }

    @Override
    public RunGroup createContainer() {

        RunGroup container = RunGroup.newThreadGroup();

        retailers = new Retailer[10];
        for (int i = 0;
                i < 10; i++) {
            retailers[i] = new Retailer();
        }

        container.addAll(retailers);
        return container;
    }

}
