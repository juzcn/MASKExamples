/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.robby;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mask.examples.robby.Robby.Action;
import mask.examples.robby.RobbyWorld.Situation;
import mask.executor.LocalModel;
import mask.executor.LocalExecutor;
import mask.executor.MasterExecutor;
import mask.rununit.RunGroup;

/**
 *
 * @author zj
 */
public class RobbyComputing extends LocalModel {

    private Action[] strategy;
    Random random1 = new Random();

    public RobbyComputing(Action[] strategy) {
        super(RobbyWorld.class);
        this.setSteps(2);
        this.strategy = strategy;
    }

    public RobbyComputing() {
        super(RobbyWorld.class);
        this.setSteps(2);
        this.strategy = new Action[situationList.size()];
        for (int i = 0; i < situationList.size(); i++) {
            strategy[i] = Action.values()[random1.nextInt(7)];
        }
    }

    @Override
    public void setup() {
    }

    @Override
    public RunGroup createContainer() {
        RunGroup container = RunGroup.newLoopGroup();
        container.add(new Robby(this.strategy));
        return container;
    }
    public static List<Situation> situationList = new ArrayList<>();

    static {
        RobbyWorld.Situation situation;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        for (int m = 0; m < 3; m++) {
                            situation = new RobbyWorld.Situation();
//                            action = Robby.Action.values()[random.nextInt(7)];
                            if (i == 0) {
                                situation.setSite(RobbyWorld.CellState.Empty);
                            } else {
                                situation.setSite(RobbyWorld.CellState.Soda);
                            }

                            if (j == 0) {
                                situation.setAround(RobbyWorld.Direction.East, RobbyWorld.CellState.Empty);
                            } else if (j == 1) {
                                situation.setAround(RobbyWorld.Direction.East, RobbyWorld.CellState.Soda);
                            } else {
                                situation.setAround(RobbyWorld.Direction.East, null);
                            }
                            if (k == 0) {
                                situation.setAround(RobbyWorld.Direction.South, RobbyWorld.CellState.Empty);
                            } else if (k == 1) {
                                situation.setAround(RobbyWorld.Direction.South, RobbyWorld.CellState.Soda);
                            } else {
                                situation.setAround(RobbyWorld.Direction.South, null);
                            }
                            if (l == 0) {
                                situation.setAround(RobbyWorld.Direction.West, RobbyWorld.CellState.Empty);
                            } else if (l == 1) {
                                situation.setAround(RobbyWorld.Direction.West, RobbyWorld.CellState.Soda);
                            } else {
                                situation.setAround(RobbyWorld.Direction.West, null);
                            }
                            if (m == 0) {
                                situation.setAround(RobbyWorld.Direction.North, RobbyWorld.CellState.Empty);
                            } else if (m == 1) {
                                situation.setAround(RobbyWorld.Direction.North, RobbyWorld.CellState.Soda);
                            } else {
                                situation.setAround(RobbyWorld.Direction.North, null);
                            }
                            situationList.add(situation);
                        }
                    }
                }
            }
        }

    }

    public static void main(String args[]) {
        System.out.println("Situation List size = " + situationList.size());

        Random random = new Random();
        Action[][] initialPopulation = new Action[200][situationList.size()];
        for (int i = 0;
                i < 200; i++) {
            for (int j = 0; j < situationList.size(); j++) {
                initialPopulation[i][j] = Action.values()[random.nextInt(7)];
            }
        }

        for (int i = 0; i < 1; i++) {
            System.out.println("Strategy " + i);
            for (int j = 0; j < 1; j++) {
                System.out.println("Round " + j);
                LocalExecutor executor = MasterExecutor.newLocalExecutor(new RobbyComputing(initialPopulation[i]));
                executor.start(200);
                System.out.println("Round "+j + " Result = "+executor.getResult());
            }
        }

    }
}
