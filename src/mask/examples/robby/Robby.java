/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.robby;

import mask.agent.SimpleAgent;
import java.util.Random;

/**
 *
 * @author zj
 */
public class Robby extends SimpleAgent {
    
    private static final Random random = new Random();
    private int row, column;
    private final Action[] stragtegy;
    
    public Robby(Action[] strategy) {
        row = 0;
        column = 0;
        this.stragtegy = strategy;
    }
    private RobbyWorld.Situation situation;
    
    @Override
    public boolean perceive() {
        situation = ((RobbyWorld) world()).getSituation(row, column);
        return false;
    }
    
    public static enum Action {
        MoveNorth, MoveSouth, MoveEast, MoveWest, RandomMove, StayPut, PickUp
    }
    private int rewards = 0;
    private boolean success;
    
    @Override
    public boolean actuate() {
        int situationIndex = RobbyComputing.situationList.indexOf(situation);
//        Action action = this.stragtegy[situationIndex];
//        Action action = strategyTable.get(situation);
        Action action = Action.values()[random.nextInt(7)];
        if (action == Action.RandomMove) {
            action = Action.values()[random.nextInt(4)];
        }
        switch (action) {
            case MoveNorth:
                success = ((RobbyWorld) world()).move(RobbyWorld.Direction.North);
                if (success) {
                    row--;
                } else {
                    rewards -= 5;
                }
                break;
            case MoveSouth:
                success = ((RobbyWorld) world()).move(RobbyWorld.Direction.South);
                if (success) {
                    row++;
                } else {
                    rewards -= 5;
                }
                break;
            case MoveEast:
                success = ((RobbyWorld) world()).move(RobbyWorld.Direction.East);
                if (success) {
                    column++;
                } else {
                    rewards -= 5;
                }
                break;
            case MoveWest:
                success = ((RobbyWorld) world()).move(RobbyWorld.Direction.West);
                if (success) {
                    column--;
                } else {
                    rewards -= 5;
                }
                break;
            case StayPut:
                break;
            case PickUp:
                success = ((RobbyWorld) world()).pickup();
                if (success) {
                    rewards += 10;
                } else {
                    rewards -= 1;
                }
                break;
        }
        return false;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }
    
    @Override
    public void stop() {
        System.out.println("Total rewards : " + rewards);
        service().writeResult(rewards);
    }
}
