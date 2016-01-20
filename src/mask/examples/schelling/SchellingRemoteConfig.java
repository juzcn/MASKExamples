/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.schelling;

import mask.executor.LocalExecutor;
import mask.rununit.RunGroup;
import java.util.Random;
import mask.executor.RemoteConfig;

/**
 *
 * @author zj
 */
public class SchellingRemoteConfig extends RemoteConfig<ISchelling> {

    private transient Random random;
    private transient Person[] persons;

    public SchellingRemoteConfig() {
        super("Remote1");
    }

    @Override
    public void setup() {
        int r;
        int row, column;
        boolean success;
        for (int i = 0; i < 8000; i++) {
            do {
                r = random.nextInt(10000);
                row = r / 100;
                column = r % 100;
                success = persons[i].moveTo(row, column);
            } while (!success);
        }
        System.out.println("initialize OK");

    }

    public static void main(String args[]) {
        LocalExecutor.newRemoteExecutor("Remote1").run();
    }

    @Override
    public RunGroup createContainer() {
        random=new Random();
        persons=new Person[8000];
        RunGroup container = RunGroup.newThreadPoolGroup(10);
        for (int i = 0; i < 8000; i++) {
            persons[i] = new Person();
        }
        container.addAll(persons);
        return container;
    }

}
