/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

import mask.agent.Agent;
import mask.agent.Behavior;
import mask.agent.Condition;

/**
 *
 * @author zj
 */
public class Company extends Agent {

    private int inventory;
    private int periodOrder;

    protected static enum State {
        WaitingProducts, WaitingOrders, WaitingReplenishmentFeedback
    };

    private int productChannel;
    private int orderChannel;
    private int replenishmnetChannel;

    @Override
    public void setup() {
        super.setup();

        state = State.WaitingProducts;
        productChannel = service().newNamedChannel(this.getUniqueID(), "Product");
        orderChannel = service().newNamedChannel(this.getUniqueID(), "Order");
        replenishmnetChannel = service().newNamedChannel(this.getUniqueID(), "Replenishment");
        setStateBehaviors(State.WaitingProducts, new Behavior(Condition.TRUE, () -> waitingProducts()));
        setStateBehaviors(State.WaitingOrders, new Behavior(Condition.TRUE, () -> waitingOrders()));
        setStateBehaviors(State.WaitingReplenishmentFeedback, new Behavior(Condition.TRUE, () -> waitingReplenishmentFeedback()));

    }

    public boolean waitingProducts() {
        System.out.println(this + " waitingProducts");
        return false;
    }

    public boolean waitingOrders() {
        System.out.println(this + " waitingOrders");
        return true;
    }

    public boolean waitingReplenishmentFeedback() {
        System.out.println(this + " waitingReplenishmentFeedback");
        return true;
    }
}
