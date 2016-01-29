/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.serviceHall;

import mask.executor.DistributedModel;
import mask.executor.MasterExecutor;

/**
 *
 * @author zj
 */
public class ServiceHallDistributed {

    public static void main(String args[]) {
//        DSExecutor.newExecutor(new BeerGameDistributed(), null).run();
        DistributedModel config = new DistributedModel("java:global.MASKBeans.MASKBeans-ejb.ServiceHallBean", new Remote1Config());
        MasterExecutor.newDistributedExecutor(config).run();
    }

}
