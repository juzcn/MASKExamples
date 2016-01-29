/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.serviceHall;

import mask.executor.LocalExecutor;
import mask.executor.LocalModel;
import mask.rununit.RunGroup;

/**
 *
 * @author zj
 */
public class ServiceHallConfig extends LocalModel<ServiceH> {

    public ServiceHallConfig() {
        super(ServiceH.class);
    }



    @Override
    public void setup() {
    }


    public static void main(String args[]) {
        LocalExecutor executor = LocalExecutor.newLocalExecutor(new ServiceHallConfig(), null);
        executor.start(1000);
    }

    @Override
    public RunGroup createContainer() {
        RunGroup container = RunGroup.newThreadPoolGroup(2);
        container.add(new CreateCustomer(100));
        return container;
    }

}
