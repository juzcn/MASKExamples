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

    public SupplyChain() {
        super();
    }

    @Override
    public RunGroup createContainer() {
        RunGroup container = RunGroup.newThreadGroup();
        Company company = new Company();
        container.add(company);
        return container;
    }

    @Override
    public void setup() {
    }

    public static void main(String args[]) {
        SupplyChain sc = new SupplyChain();
        LocalExecutor executor = MasterExecutor.newLocalComputing(sc);
        executor.run();
    }
}
