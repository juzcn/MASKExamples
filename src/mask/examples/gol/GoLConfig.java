/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.gol;

import mask.executor.LocalExecutor;
import mask.executor.LocalModel;
import mask.rununit.RunGroup;

/**
 *
 * @author zj
 */
public class GoLConfig extends LocalModel<GoL> {

    private CellAgent[] cells = new CellAgent[10000];

    public GoLConfig() {
        super(GoL.class);
        this.setSteps(2);
    }

    @Override
    public void setup() {
        int row, column;
        boolean success;
        for (int i = 0; i < 10000; i++) {
            row = i / 100;
            column = i % 100;
            cells[i].setRow(row);
            cells[i].setColumn(column);
            world.getGridSpace().setValue(row, column, CellAgent.aliveBean.getBean(cells[i]));
        }
        System.out.println("initialize OK");
    }

    public static void main(String args[]) {
        LocalExecutor executor = LocalExecutor.newLocalExecutor(new GoLConfig(), null);
        executor.start(100);
    }

    @Override
    public RunGroup createContainer() {
        RunGroup container = RunGroup.newLoopGroup();
        for (int i = 0; i < 10000; i++) {
            cells[i] = new CellAgent();
        }
        container.addAll(cells);
        return container;
    }

}
