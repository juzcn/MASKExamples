/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame.distributed;

import mask.examples.beergame.BeerGameCSVLogger;
import mask.examples.beergame.BeerGameXSLLogger;
import mask.executor.DistributedModel;
import mask.executor.MKExecutor;

/**
 *
 * @author zj
 */
public class BeerGameDistributed  {

    public static void main(String args[]) {
        DistributedModel config=new DistributedModel("java:global.MASKBeans.MASKBeans-ejb.BeerGame", new Remote1Config(),new Remote2Config());
        MKExecutor.newDistributedExecutor(config, null, new BeerGameCSVLogger(), new BeerGameXSLLogger()).run();
    }

}
