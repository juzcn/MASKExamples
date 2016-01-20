/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.serviceHall;

import mask.executor.MKExecutor;
import mask.executor.RemoteConfig;
import mask.rununit.RunGroup;

/**
 *
 * @author zj
 */
public class Remote1Config extends RemoteConfig<IServiceHall> {

    public Remote1Config() {
        super("Remote1");
    }



    @Override
    public void setup() {
    }

    public static void main(String args[]) {
        MKExecutor.newRemoteExecutor("Remote1").run();
    }

    @Override
    public RunGroup createContainer() {

        RunGroup container = RunGroup.newLoopGroup();
        container.add(new CreateCustomer(100));
        return container;
    }


}
