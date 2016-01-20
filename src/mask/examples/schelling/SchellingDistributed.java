/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.schelling;

import mask.executor.DistributedConfig;
import mask.executor.MKExecutor;

/**
 *
 * @author zj
 */
public class SchellingDistributed {

    public static void main(String args[]) {
        DistributedConfig config = new DistributedConfig("java:global.MASKBeans.MASKBeans-ejb.SchellingBean", new SchellingRemoteConfig());
        config.setSteps(2);
        MKExecutor.newDistributedExecutor(config, null).run();
    }

}
